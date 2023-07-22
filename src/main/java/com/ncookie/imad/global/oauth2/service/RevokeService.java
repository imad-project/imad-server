package com.ncookie.imad.global.oauth2.service;

import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

import static com.ncookie.imad.global.Utils.extractToken;

@Slf4j
@RequiredArgsConstructor
@Service
public class RevokeService {
    private final UserAccountRepository userRepository;
    private final JwtService jwtService;

    private final AppleService appleService;


    public void deleteAppleAccount(String accessToken) throws IOException {
        String appleRevokeUrl = "https://appleid.apple.com/auth/revoke";
        RestTemplate restTemplate = new RestTemplate();

        Optional<String> email = jwtService.extractClaimFromJWT(JwtService.CLAIM_EMAIL, extractToken(accessToken));
        if (email.isEmpty()) {
            throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
        }

        Optional<UserAccount> userAccount = userRepository.findByEmail(email.get());
        if (userAccount.isEmpty()) {
            throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
        }

        // 유저 관련 데이터 DB에서 삭제
        // TODO: 추후 DB 테이블 추가 시 관련 데이터 삭제 구문 구현 필요
        userRepository.delete(userAccount.get());

        String data = "client_id=" + appleService.getAPPLE_CLIENT_ID() +
                "&client_secret=" + appleService.createClientSecretKey() +
                "&token=" + userAccount.get().getOauth2AccessToken() +
                "&token_type_hint=access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(data, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(appleRevokeUrl, HttpMethod.POST, entity, String.class);

        // Get the response status code and body
        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response: " + responseBody);
    }
}
