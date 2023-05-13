package com.ncookie.imad.global.oauth2.service;

import com.ncookie.imad.global.dto.request.TokenRequest;
import com.ncookie.imad.domain.user.dto.response.SignInResponse;
import com.ncookie.imad.global.dto.response.TokenResponse;

public class GoogleRequestService implements RequestService {
    @Override
    public SignInResponse redirect(TokenRequest tokenRequest) {
        return null;
    }

    @Override
    public TokenResponse getToken(TokenRequest tokenRequest) {
        return null;
    }

    @Override
    public Object getUserInfo(String accessToken) {
        return null;
    }

    @Override
    public TokenResponse getRefreshToken(String provider, String refreshToken) {
        return null;
    }
}
