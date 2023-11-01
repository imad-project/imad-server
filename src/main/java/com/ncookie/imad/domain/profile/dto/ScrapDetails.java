package com.ncookie.imad.domain.profile.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.profile.entity.PostingScrap;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScrapDetails {
    private Long scrapId;

    private Long userId;

    private Long postingId;                // 게시글 ID
    private String postingTitle;           // 게시글 제목

    private LocalDateTime createdDate;

    public static ScrapDetails toDTO(PostingScrap scrap) {
        return ScrapDetails.builder()
                .scrapId(scrap.getId())
                .userId(scrap.getUserAccount().getId())

                .postingId(scrap.getPosting().getPostingId())
                .postingTitle(scrap.getPosting().getTitle())

                .createdDate(scrap.getCreatedDate())
                .build();
    }
}
