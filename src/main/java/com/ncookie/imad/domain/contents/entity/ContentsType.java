package com.ncookie.imad.domain.contents.entity;

public enum ContentsType {
    MOVIE("MOVIE"),
    TV("TV"),
    ANIMATION("ANIMATION");

    private final String contentsType;

    public String getContentsType() {
        return contentsType;
    }

    ContentsType(String contentsType) {
        this.contentsType = contentsType;
    }
}
