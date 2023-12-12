package com.ncookie.imad.domain.profile.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileSummaryInfoResponse {

    // 유저 정보
    private Long userId;                    // 유저 id
    private String userNickname;            // 닉네임
    private int userProfileImage;           // 프로필 이미지
    
    private int myReviewCnt;                // 내 리뷰 개수
    private int myPostingCnt;               // 내 게시글 개수
    private int myScrapCnt;                 // 내 스크랩 개수
    
    private BookmarkListResponse bookmarkListResponse;
}
