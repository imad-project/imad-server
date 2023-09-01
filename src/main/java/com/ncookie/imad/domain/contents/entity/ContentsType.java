package com.ncookie.imad.domain.contents.entity;

import lombok.Getter;

// 작품 상세 페이지에서 보여주는 작품 종류의 ENUM 클래스
@Getter
public enum ContentsType {
    MOVIE("MOVIE"),
    TV("TV"),
    ANIMATION("ANIMATION");         // 장르에 "애니메이션"이 포함되어 있으면 TV, MOVIE가 아닌 ANIMATION으로 분류함

    private final String contentsType;

    ContentsType(String contentsType) {
        this.contentsType = contentsType;
    }
}
