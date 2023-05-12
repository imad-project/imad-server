package com.ncookie.imad.global.oauth2.service;

import com.ncookie.imad.domain.user.entity.AuthProvider;
import com.ncookie.imad.global.oauth2.userinfo.KakaoUserInfo;
import com.ncookie.imad.global.dto.request.TokenRequest;
import com.ncookie.imad.global.dto.response.SignInResponse;
import com.ncookie.imad.global.dto.response.TokenResponse;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.security.SecurityUtil;
import com.ncookie.imad.domain.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class KakaoRequestService implements RequestService {
    private final UserAccountRepository userAccountRepository;
    private final UserAccountService userAccountService;

    private final SecurityUtil securityUtil;
    private final WebClient webClient;


    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.token_uri}")
    private String TOKEN_URI;

    @Override
    public SignInResponse redirect(TokenRequest tokenRequest) {
        TokenResponse tokenResponse = getToken(tokenRequest);
        KakaoUserInfo kakaoUserInfo = getUserInfo(tokenResponse.getAccessToken());

        String accessToken = securityUtil.createAccessToken(
                String.valueOf(kakaoUserInfo.getId()), AuthProvider.KAKAO, tokenResponse.getAccessToken());
        String refreshToken = securityUtil.createRefreshToken(
                String.valueOf(kakaoUserInfo.getId()), AuthProvider.KAKAO, tokenResponse.getRefreshToken());

        if (!userAccountRepository.existsByUserIdAndAuthProvider(String.valueOf(kakaoUserInfo.getId()), AuthProvider.KAKAO)) {
            // 신규 회원가입

//            userAccountService.createOauthUserAccount(String.valueOf(kakaoUserInfo.getId()),
//                    AuthProvider.KAKAO,
//                    kakaoUserInfo.getKakaoAccount().getProfile().getProfileImageUrl);


        } else {
            // refresh 토큰이 만료되어 다시 로그인을 하려고 할 때

        }


        return SignInResponse.builder()
                .authProvider(AuthProvider.KAKAO)
                .kakaoUserInfo(kakaoUserInfo)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 인증 서버로부터 access token 을 받아옴
    @Override
    public TokenResponse getToken(TokenRequest tokenRequest) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE);
        formData.add("redirect_uri", REDIRECT_URI);
        formData.add("client_id", CLIENT_ID);
        formData.add("code", tokenRequest.getCode());

        return webClient.mutate()
                .baseUrl(TOKEN_URI)
                .build()
                .post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new BadRequestException()))
                .bodyToMono(TokenResponse.class)
                .block();   // 여기서 에러 발생
    }

    @Override
    public KakaoUserInfo getUserInfo(String accessToken) {
        return webClient.mutate()
                .baseUrl("https://kapi.kakao.com")
                .build()
                .get()
                .uri("/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }

    @Override
    public TokenResponse getRefreshToken(String provider, String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", CLIENT_ID);
        formData.add("refresh_token", refreshToken);

        return webClient.mutate()
                .baseUrl("https://kauth.kakao.com")
                .build()
                .post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new BadRequestException()))
                .bodyToMono(TokenResponse.class)
                .block();
    }
}
