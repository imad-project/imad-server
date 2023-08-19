package com.ncookie.imad.domain.contents.entity;

import lombok.Getter;

@Getter
public enum ContentsType {
    MOVIE("MOVIE"),
    TV("TV"),
    ANIMATION("ANIMATION");

    private final String contentsType;

    ContentsType(String contentsType) {
        this.contentsType = contentsType;
    }
}
