package com.ncookie.imad.domain.user.dto.request;

import com.ncookie.imad.domain.user.entity.AuthProvider;
import com.ncookie.imad.domain.user.entity.Gender;
import lombok.Getter;

@Getter
public class SignUpRequest {
    private String email;
    private String password;
    private String nickname;
    private AuthProvider authProvider;
}
