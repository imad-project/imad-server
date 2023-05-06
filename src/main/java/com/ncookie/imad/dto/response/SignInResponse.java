package com.ncookie.imad.dto.response;

import com.ncookie.imad.domain.type.AuthProvider;
import com.ncookie.imad.dto.AppleUserInfo;
import com.ncookie.imad.dto.GoogleUserInfo;
import com.ncookie.imad.dto.KakaoUserInfo;
import com.ncookie.imad.dto.NaverUserInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponse {
    private final AuthProvider authProvider;
    private final AppleUserInfo appleUserInfo;
    private final GoogleUserInfo googleUserInfo;
    private final KakaoUserInfo kakaoUserInfo;
    private final NaverUserInfo naverUserInfo;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public SignInResponse(
            AuthProvider authProvider,
            AppleUserInfo appleUserInfo,
            KakaoUserInfo kakaoUserInfo,
            NaverUserInfo naverUserInfo,
            GoogleUserInfo googleUserInfo,
            String accessToken,
            String refreshToken
    ) {
        this.authProvider = authProvider;
        this.appleUserInfo = appleUserInfo;
        this.googleUserInfo = googleUserInfo;
        this.kakaoUserInfo = kakaoUserInfo;
        this.naverUserInfo = naverUserInfo;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
