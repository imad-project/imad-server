package com.ncookie.imad.domain.review.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ModifyReviewRequest {
    private String title;               // 리뷰 제목
    private String content;             // 리뷰 본문

    private float score;                 // 리뷰 점수

    @JsonProperty("is_spoiler")
    private boolean isSpoiler;          // 스포일러 여부
}
