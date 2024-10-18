package com.ncookie.imad.domain.recommend.data;

import lombok.Getter;

@Getter
public enum RecommendCategory {
    ALL("ALL"),
    POPULAR("POPULAR"),
    TOP_RATED("TOP_RATED");

    private final String category;

    private RecommendCategory(String category) {
        this.category = category;
    }
}
