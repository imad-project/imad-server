package com.ncookie.imad.global.oauth2.service;

import com.ncookie.imad.global.dto.request.TokenRequest;
import com.ncookie.imad.global.dto.response.SignInResponse;
import com.ncookie.imad.global.dto.response.TokenResponse;

public interface RequestService<T> {
    SignInResponse redirect(TokenRequest tokenRequest);
    TokenResponse getToken(TokenRequest tokenRequest);
    T getUserInfo(String accessToken);
    TokenResponse getRefreshToken(String provider, String refreshToken);
}
