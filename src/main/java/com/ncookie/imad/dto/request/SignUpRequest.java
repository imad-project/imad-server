package com.ncookie.imad.dto.request;

import com.ncookie.imad.domain.type.AuthProvider;
import com.ncookie.imad.domain.type.Gender;
import lombok.Getter;

@Getter
public class SignUpRequest {
    private String id;
    private String email;
    private String nickname;
    private Gender gender;
    private int ageRange;
    private String profileImageUrl;
    private AuthProvider authProvider;
}
