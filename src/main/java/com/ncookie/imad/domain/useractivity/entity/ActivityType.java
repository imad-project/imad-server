package com.ncookie.imad.domain.useractivity.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
    CONTENTS_BOOKMARK("CONTENTS_BOOKMARK", "활동: 작품 찜"),
    WRITING_REVIEW("WRITING_REVIEW", "활동: 리뷰 작성"),
    WRITING_POSTING("WRITING_POSTING", "활동: 게시글 작성"),
    LIKE_REVIEW("LIKE_REVIEW", "활동: 리뷰 좋아요"),
    LIKE_POSTING("LIKE_POSTING", "활동: 게시글 좋아요");

    private final String key;
    private final String activity;
}
