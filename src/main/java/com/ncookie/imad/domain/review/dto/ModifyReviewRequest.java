package com.ncookie.imad.domain.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ModifyReviewRequest {
    private String title;               // 리뷰 제목
    private String content;             // 리뷰 본문

    private float score;                 // 리뷰 점수
    private boolean isSpoiler;          // 스포일러 여부
}