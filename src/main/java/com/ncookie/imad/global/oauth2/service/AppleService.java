package com.ncookie.imad.global.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.user.entity.AuthProvider;
import com.ncookie.imad.domain.user.entity.Role;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.global.jwt.service.JwtService;
import com.ncookie.imad.global.oauth2.dto.AppleLoginResponse;
import com.ncookie.imad.global.oauth2.dto.AppleToken;
import com.ncookie.imad.global.oauth2.property.AppleProperties;
import com.ncookie.imad.global.oauth2.utils.AppleJwtUtils;
import com.ncookie.imad.global.openfeign.AppleLoginClient;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.simple.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.security.PrivateKey;
import java.util.*;

import static com.ncookie.imad.global.Utils.logWithOauthProvider;
import static com.ncookie.imad.global.oauth2.utils.AppleJwtUtils.APPLE_AUTH_URL;

@Slf4j
@EnableConfigurationProperties({ AppleProperties.class })
@RequiredArgsConstructor
@Service
public class AppleService {

    private final UserAccountRepository userRepository;
    private final JwtService jwtService;

    private final AppleProperties appleProperties;
    private final AppleJwtUtils appleJwtUtils;
    private final AppleLoginClient appleLoginClient;


    public String getAppleLoginUrl(String redirectUri) {
        String loginUrl = APPLE_AUTH_URL + "/auth/authorize"
                + "?client_id=" + appleProperties.getClientId()
                + "&redirect_uri=" + appleProperties.getRedirectUrl()
                + "&response_type=code%20id_token&scope=name%20email&response_mode=form_post";

        if (redirectUri != null && !redirectUri.isEmpty()) {
            loginUrl = loginUrl + "&state=" + redirectUri;
            log.info("리액트에서 애플 로그인 요청 시도 : redirect_uri를 state 파라미터에 추가");
        }

        log.info("애플 로그인 URL 반환");
        return loginUrl;
    }

    // 안드로이드 앱 / 리액트 웹 서비스 전용 애플 로그인
    public UserAccount loginWithRest(AppleLoginResponse appleLoginResponse) {
        return login(appleLoginResponse, false);
    }

    // iOS 앱 전용 애플 로그인. JWT 토큰을 애플 측에서 받아 다이렉트로 보내줌
    public UserAccount loginWithToken(AppleLoginResponse appleLoginResponse) {
        return login(appleLoginResponse, true);
    }

    public UserAccount login(AppleLoginResponse appleLoginResponse, boolean isiOSApp) {
        String userId;
        String email;
        String accessToken;

        UserAccount user;

        try {
            // ID TOKEN 검증
            boolean isVerify = appleJwtUtils.verifyIdentityToken(appleLoginResponse.getIdToken(), isiOSApp);
            if (!isVerify) {
                throw new BadRequestException(ResponseCode.OAUTH2_APPLE_ID_TOKEN_INVALID);
            }

            AppleToken.Response tokenResponse = generateAuthToken(appleLoginResponse.getCode(), isiOSApp);

            accessToken = tokenResponse.getAccessToken();

            // ID TOKEN을 통해 회원 고유 식별자 받기
            SignedJWT signedJWT = SignedJWT.parse(String.valueOf(tokenResponse.getIdToken()));
            ReadOnlyJWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject payload = objectMapper.readValue(jwtClaimsSet.toJSONObject().toJSONString(), JSONObject.class);

            userId = String.valueOf(payload.get("sub"));
            email = String.valueOf(payload.get("email"));

            UserAccount findUser = userRepository
                    .findByAuthProviderAndSocialId(AuthProvider.APPLE, userId)
                    .orElse(null);

            if (findUser == null) {
                // 신규 회원가입의 경우 DB에 저장
                logWithOauthProvider(AuthProvider.APPLE, "신규 회원가입 DB 저장");
                user = userRepository.save(
                        UserAccount.builder()
                                .authProvider(AuthProvider.APPLE)
                                .socialId(userId)
                                .email(email)
                                .role(Role.GUEST)
                                .oauth2AccessToken(accessToken)
                                .refreshToken(jwtService.createRefreshToken())
                                .build()
                );
            } else {
                // 기존 회원의 경우 access token 업데이트를 위해 DB에 저장
                logWithOauthProvider(AuthProvider.APPLE, "기존 회원 DB 업데이트");
                findUser.setOauth2AccessToken(accessToken);
                user = userRepository.save(findUser);
            }

            return user;

        } catch (JsonProcessingException | java.text.ParseException e) {
            throw new BadRequestException(ResponseCode.OAUTH2_APPLE_TOKEN_INVALID);
        }
    }

    public void loginSuccess(UserAccount user, HttpServletResponse response) {
        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(user.getEmail(), refreshToken);
    }

    public String determineSuccessRedirectUrl(UserAccount user, String baseUrl) {
        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        jwtService.updateRefreshToken(user.getEmail(), refreshToken);

        return UriComponentsBuilder.fromUriString(baseUrl)
                .path("/success")
                .queryParam("token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString();
    }

    public String determineFailureRedirectUrl(String baseUrl) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .path("/fail")
                .build().toUriString();
    }

    public AppleToken.Response generateAuthToken(String code, boolean isiOSApp) {
        if (code == null) throw new IllegalArgumentException("Failed get authorization code");

        String clientId = isiOSApp ? appleProperties.getIOSClientId() : appleProperties.getClientId();
        return appleLoginClient.getToken(AppleToken.Request.of(
                code,
                clientId,
                createClientSecretKey(clientId),
                "authorization_code",
                null
        ));
    }

    public String createClientSecretKey(String clientId) {
        // headerParams 적재
        Map<String, Object> headerParamsMap = new HashMap<>();
        headerParamsMap.put("kid", appleProperties.getLoginKey());
        headerParamsMap.put("alg", "ES256");

        // clientSecretKey 생성
        try {
            return Jwts
                    .builder()
                    .setHeaderParams(headerParamsMap)
                    .setIssuer(appleProperties.getTeamId())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30)) // 만료 시간 (30초)
                    .setAudience(APPLE_AUTH_URL)
                    .setSubject(clientId)
                    .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                    .compact();
        } catch (IOException e) {
            throw new BadRequestException(ResponseCode.OAUTH2_APPLE_TOKEN_INVALID);
        }
    }

    public String getAppleClientId() {
        return appleProperties.getClientId();
    }

    public String getIOSAppleClientId() {
        return appleProperties.getIOSClientId();
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(appleProperties.getKeyPath());
        String privateKey = new String(resource.getInputStream().readAllBytes());

        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();

        return converter.getPrivateKey(object);
    }
}
