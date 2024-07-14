package com.ncookie.imad.domain.profile.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.profile.entity.PostingScrap;
import com.ncookie.imad.domain.user.entity.UserAccount;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScrapDetails {
    private Long scrapId;

    // 유저 정보
    private Long userId;                    // 유저 id
    private String userNickname;            // 닉네임
    private String userProfileImage;           // 프로필 이미지

    // 작품 정보
    private Long contentsId;                // 작품 ID
    private String contentsTitle;           // 작품 제목
    private String contentsPosterPath;      // 작품 포스터 이미지 경로

    private Long postingId;                // 게시글 ID
    private String postingTitle;           // 게시글 제목

    private LocalDateTime createdDate;

    public static ScrapDetails toDTO(PostingScrap scrap) {
        UserAccount userAccount = scrap.getPosting().getUser();
        Contents contents = scrap.getPosting().getContents();

        return ScrapDetails.builder()
                .scrapId(scrap.getId())

                .userId(userAccount.getId())
                .userNickname(userAccount.getNickname())
                .userProfileImage(userAccount.getProfileImage())

                .contentsId(contents.getContentsId())
                .contentsTitle(contents.getTranslatedTitle())
                .contentsPosterPath(contents.getPosterPath())

                .postingId(scrap.getPosting().getPostingId())
                .postingTitle(scrap.getPosting().getTitle())

                .createdDate(scrap.getCreatedDate())
                .build();
    }
}
