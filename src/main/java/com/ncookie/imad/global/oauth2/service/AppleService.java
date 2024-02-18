package com.ncookie.imad.global.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.user.entity.AuthProvider;
import com.ncookie.imad.domain.user.entity.Role;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.jwt.service.JwtService;
import com.ncookie.imad.global.oauth2.property.AppleProperties;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.security.PrivateKey;
import java.util.*;

import static com.ncookie.imad.global.Utils.logWithOauthProvider;

@Slf4j
@EnableConfigurationProperties({ AppleProperties.class })
@RequiredArgsConstructor
@Service
public class AppleService {

    private final UserAccountRepository userRepository;
    private final JwtService jwtService;

    private final AppleProperties appleProperties;


    private final static String APPLE_AUTH_URL = "https://appleid.apple.com";

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

    public UserAccount login(String code) {
        String userId;
        String email;
        String accessToken;

        UserAccount user;

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(generateAuthToken(code));

            accessToken = String.valueOf(jsonObj.get("access_token"));

            // ID TOKEN을 통해 회원 고유 식별자 받기
            SignedJWT signedJWT = SignedJWT.parse(String.valueOf(jsonObj.get("id_token")));
            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();

            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), JSONObject.class);

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

        } catch (ParseException | JsonProcessingException e) {
            throw new RuntimeException("Failed to parse json data");
        } catch (IOException | java.text.ParseException e) {
            throw new RuntimeException(e);
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

    public String generateAuthToken(String code) throws IOException {
        if (code == null) throw new IllegalArgumentException("Failed get authorization code");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", appleProperties.getClientId());
        params.add("client_secret", createClientSecretKey());
        params.add("code", code);
        params.add("redirect_uri", appleProperties.getRedirectUrl());

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    APPLE_AUTH_URL + "/auth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("Apple Auth Token Error");
        }
    }

    public String createClientSecretKey() throws IOException {
        // headerParams 적재
        Map<String, Object> headerParamsMap = new HashMap<>();
        headerParamsMap.put("kid", appleProperties.getLoginKey());
        headerParamsMap.put("alg", "ES256");

        // clientSecretKey 생성
        return Jwts
                .builder()
                .setHeaderParams(headerParamsMap)
                .setIssuer(appleProperties.getTeamId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30)) // 만료 시간 (30초)
                .setAudience(APPLE_AUTH_URL)
                .setSubject(appleProperties.getClientId())
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    public String getAppleClientId() {
        return appleProperties.getClientId();
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
