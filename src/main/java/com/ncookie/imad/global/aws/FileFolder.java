package com.ncookie.imad.global.aws;

import lombok.Getter;

@Getter
public enum FileFolder {
    PROFILE_IMAGES("profile/"),
    POSTING_IMAGES("posting/");

    private final String value;

    FileFolder(String value) {
        this.value = value;
    }
}
