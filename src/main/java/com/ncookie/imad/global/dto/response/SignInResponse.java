package com.ncookie.imad.global.dto.response;

import com.ncookie.imad.domain.user.entity.AuthProvider;
import com.ncookie.imad.global.oauth2.userinfo.AppleUserInfo;
import com.ncookie.imad.global.oauth2.userinfo.GoogleUserInfo;
import com.ncookie.imad.global.oauth2.userinfo.KakaoUserInfo;
import com.ncookie.imad.global.oauth2.userinfo.NaverUserInfo;
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
