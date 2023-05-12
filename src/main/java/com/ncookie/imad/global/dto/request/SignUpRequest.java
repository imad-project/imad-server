package com.ncookie.imad.global.dto.request;

import com.ncookie.imad.domain.user.entity.AuthProvider;
import com.ncookie.imad.domain.user.entity.Gender;
import lombok.Getter;

@Getter
public class SignUpRequest {
    private String id;
    private String password;
    private String email;
    private String nickname;
    private Gender gender;
    private int ageRange;
    private String profileImageUrl;
    private AuthProvider authProvider;
}
