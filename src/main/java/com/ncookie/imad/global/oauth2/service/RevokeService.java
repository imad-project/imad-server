package com.ncookie.imad.global.oauth2.service;

import com.ncookie.imad.domain.user.entity.AuthProvider;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

import static com.ncookie.imad.global.Utils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RevokeService {
    private final UserAccountRepository userRepository;
    private final JwtService jwtService;

    private final AppleService appleService;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;


    public void deleteAppleAccount(String accessToken) throws IOException {
        UserAccount userAccount = extractUserFromAccessToken(accessToken);
        deleteUserAccount(userAccount);

        String data = "client_id=" + appleService.getAppleClientId() +
                "&client_secret=" + appleService.createClientSecretKey() +
                "&token=" + userAccount.getOauth2AccessToken() +
                "&token_type_hint=access_token";

        sendRevokeRequest(data, AuthProvider.APPLE, null);
    }

    public void deleteGoogleAccount(String accessToken) {
        UserAccount userAccount = extractUserFromAccessToken(accessToken);
        deleteUserAccount(userAccount);

        String data = "token=" + userAccount.getOauth2AccessToken();

        sendRevokeRequest(data, AuthProvider.GOOGLE, null);
    }

    public void deleteNaverAccount(String accessToken) {
        UserAccount userAccount = extractUserFromAccessToken(accessToken);
        deleteUserAccount(userAccount);

        String data = "client_id=" + naverClientId +
                "&client_secret=" + naverClientSecret +
                "&access_token=" + userAccount.getOauth2AccessToken() +
                "&service_provider=NAVER" +
                "&grant_type=delete";

        sendRevokeRequest(data, AuthProvider.NAVER, null);
    }

    public void deleteKakaoAccount(String accessToken) {
        UserAccount userAccount = extractUserFromAccessToken(accessToken);
        deleteUserAccount(userAccount);

        sendRevokeRequest(null, AuthProvider.KAKAO, userAccount.getOauth2AccessToken());
    }

    private UserAccount extractUserFromAccessToken(String accessToken) {
        Optional<String> email = jwtService.extractClaimFromJWT(JwtService.CLAIM_EMAIL, extractToken(accessToken));
        if (email.isEmpty()) {
            throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
        }

        Optional<UserAccount> userAccount = userRepository.findByEmail(email.get());
        if (userAccount.isEmpty()) {
            throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
        }

        return userAccount.get();
    }

    private void deleteUserAccount(UserAccount userAccount) {
        // 유저 관련 데이터 DB에서 삭제
        // TODO: 추후 DB 테이블 추가 시 관련 데이터 삭제 구문 구현 필요
        userRepository.delete(userAccount);
    }

    /**
     * @param data : revoke request의 body에 들어갈 데이터
     * @param provider : oauth2 업체
     * @param accessToken : 카카오의 경우 url이 아니라 헤더에 access token을 첨부해서 보내줘야 함
     */
    private void sendRevokeRequest(String data, AuthProvider provider, String accessToken) {
        String appleRevokeUrl = "https://appleid.apple.com/auth/revoke";
        String googleRevokeUrl = "https://accounts.google.com/o/oauth2/revoke";
        String naverRevokeUrl = "https://nid.naver.com/oauth2.0/token";
        String kakaoRevokeUrl = "https://kapi.kakao.com/v1/user/unlink";

        RestTemplate restTemplate = new RestTemplate();
        String revokeUrl = "";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(data, headers);

        switch (provider) {
            case APPLE -> revokeUrl = appleRevokeUrl;
            case GOOGLE -> revokeUrl = googleRevokeUrl;
            case NAVER -> revokeUrl = naverRevokeUrl;
            case KAKAO -> {
                revokeUrl = kakaoRevokeUrl;
                headers.setBearerAuth(accessToken);
            }
        }

        ResponseEntity<String> responseEntity = restTemplate.exchange(revokeUrl, HttpMethod.POST, entity, String.class);

        // Get the response status code and body
        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();

        logWithOauthProvider(AuthProvider.APPLE, "소셜 회원 연결해제 요청 결과");
        logWithOauthProvider(AuthProvider.APPLE, "Status Code: " + statusCode);
        logWithOauthProvider(AuthProvider.APPLE, "Response: " + responseBody);

    }
}
