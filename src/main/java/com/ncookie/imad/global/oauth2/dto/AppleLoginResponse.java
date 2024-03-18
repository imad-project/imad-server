package com.ncookie.imad.global.oauth2.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
// 사용자가 애플 로그인 성공 시 받는 응답
public class AppleLoginResponse {
    private String state;
    private String code;
    private String idToken;
    private String user;
}
