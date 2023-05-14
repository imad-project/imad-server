package com.ncookie.imad.global.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResponseStatus {
    SIGNUP_SUCCESS(200, "SIGNUP_SUCCESS"),
    LOGIN_SUCCESS(200, "LOGIN_SUCCESS");

    private final int code;
    private final String status;
}
