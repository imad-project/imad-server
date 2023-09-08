package com.ncookie.imad.domain.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewDetailsResponse {
    private Long reviewId;                  // 리뷰 ID
    private Long contentsId;                // 작품 ID
    
    // TODO: 클라이언트와 논의하여 필요한 작품 추가 정보 등에 대해 정해야함 (제목, 포스터, 개봉연도 등)

    private String title;                   // 제목
    private String content;                 // 본문

    private float score;                    // 리뷰 점수
    private boolean isSpoiler;              // 스포일러 여부

    private int likeCnt;                    // 좋아요 수
    private int dislikeCnt;                 // 싫어요 수

    private LocalDateTime createdAt;        // 리뷰 작성 날짜
    private LocalDateTime modifiedAt;       // 리뷰 수정 날짜

    private int likeStatus;                 // 1이면 좋아요, -1이면 싫어요, 0이면 아무 상태도 아님
}
