package com.ncookie.imad.domain.posting.dto.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.posting.entity.Posting;
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
public class PostingDetailsResponse {
    private Long postingId;                 // 게시글 ID

    // 작품 정보
    private Long contentsId;                // 작품 ID
    private String contentsTitle;           // 작품 제목
    private String contentsPosterPath;      // 작품 포스터 이미지 경로
    private String contentsBackdropPath;            // 작품 배경 포스터 이미지 경로

    // 유저 정보
    private Long userId;                    // 유저 id
    private String userNickname;            // 닉네임
    private int userProfileImage;           // 프로필 이미지

    // 게시글 정보
    private String title;                   // 제목
    private String content;                 // 본문
    private int category;                   // 카테고리

    private boolean isAuthor;               // 본인 작성 여부
    private boolean isSpoiler;              // 스포일러 여부

    private int viewCnt;                    // 조회수
    private int likeCnt;                    // 좋아요 수
    private int dislikeCnt;                 // 싫어요 수

    private int likeStatus;                 // 1이면 좋아요, -1이면 싫어요, 0이면 아무 상태도 아님

    private LocalDateTime createdAt;        // 리뷰 작성 날짜
    private LocalDateTime modifiedAt;       // 리뷰 수정 날짜

    // 댓글 정보
    private int commentCnt;                                     // 댓글 개수
    private CommentListResponse commentListResponse;            // 댓글 리스트

    // 스크랩 정보
    private Long scrapId;
    private boolean scrapStatus;


    public static PostingDetailsResponse toDTO(Posting posting, CommentListResponse commentList) {
        return PostingDetailsResponse.builder()
                .postingId(posting.getPostingId())

                .contentsId(posting.getContents().getContentsId())
                .contentsTitle(posting.getContents().getTranslatedTitle())
                .contentsPosterPath(posting.getContents().getPosterPath())
                .contentsBackdropPath(posting.getContents().getBackdropPath())

                .userId(posting.getUser().getId())
                .userNickname(posting.getUser().getNickname())
                .userProfileImage(posting.getUser().getProfileImage())

                .title(posting.getTitle())
                .content(posting.getContent())
                .category(posting.getCategory())

                .isSpoiler(posting.isSpoiler())

                .viewCnt(posting.getViewCnt())
                .likeCnt(posting.getLikeCnt())
                .dislikeCnt(posting.getDislikeCnt())

                .createdAt(posting.getCreatedDate())
                .modifiedAt(posting.getModifiedDate())

                .commentListResponse(commentList)

                .build();
    }
}
