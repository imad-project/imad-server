package com.ncookie.imad.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    NONE("NONE", "default"),
    MALE("MALE", "남자"),
    FEMALE("FEMALE", "여자");

    private final String key;
    private final String gender;
}
