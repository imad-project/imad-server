package com.ncookie.imad.service;

import com.ncookie.imad.dto.request.TokenRequest;
import com.ncookie.imad.dto.response.SignInResponse;
import com.ncookie.imad.dto.response.TokenResponse;

public class NaverRequestService implements RequestService {
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
