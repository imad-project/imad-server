package com.ncookie.imad.domain.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.review.entity.Review;
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
    
    // 작품 정보
    private String contentsTitle;
    private String contentsPosterPath;
    
    // 유저 정보
    private Long userId;                    // 유저 id
    private String userNickname;            // 닉네임
    private int userProfileImage;           // 프로필 이미지

    // 리뷰 정보
    private String title;                   // 제목
    private String content;                 // 본문

    private float score;                    // 리뷰 점수
    private boolean isSpoiler;              // 스포일러 여부

    private int likeCnt;                    // 좋아요 수
    private int dislikeCnt;                 // 싫어요 수

    private LocalDateTime createdAt;        // 리뷰 작성 날짜
    private LocalDateTime modifiedAt;       // 리뷰 수정 날짜

    private int likeStatus;                 // 1이면 좋아요, -1이면 싫어요, 0이면 아무 상태도 아님

    public static ReviewDetailsResponse toDTO(Review review) {
        return ReviewDetailsResponse.builder()
                .reviewId(review.getReviewId())
                .contentsId(review.getContents().getContentsId())

                .contentsTitle(review.getContents().getTranslatedTitle())
                .contentsPosterPath(review.getContents().getPosterPath())

                .userNickname(review.getUserAccount().getNickname())
                .userProfileImage(review.getUserAccount().getProfileImage())

                .title(review.getTitle())
                .content(review.getContent())

                .score(review.getScore())
                .isSpoiler(review.isSpoiler())

                .likeCnt(review.getLikeCnt())
                .dislikeCnt(review.getDislikeCnt())

                .createdAt(review.getCreatedDate())
                .modifiedAt(review.getModifiedDate())

                .build();
    }
}
