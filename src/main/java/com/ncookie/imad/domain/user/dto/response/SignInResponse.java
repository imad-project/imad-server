package com.ncookie.imad.domain.user.dto.response;

import com.ncookie.imad.domain.user.entity.AuthProvider;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponse {
    private final AuthProvider authProvider;;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public SignInResponse(
            AuthProvider authProvider,
            String accessToken,
            String refreshToken
    ) {
        this.authProvider = authProvider;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
