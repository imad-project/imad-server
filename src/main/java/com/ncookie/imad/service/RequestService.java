package com.ncookie.imad.service;

import com.ncookie.imad.dto.request.TokenRequest;
import com.ncookie.imad.dto.response.SignInResponse;
import com.ncookie.imad.dto.response.TokenResponse;

public interface RequestService<T> {
    SignInResponse redirect(TokenRequest tokenRequest);
    TokenResponse getToken(TokenRequest tokenRequest);
    T getUserInfo(String accessToken);
    TokenResponse getRefreshToken(String provider, String refreshToken);
}
