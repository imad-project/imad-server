package com.ncookie.imad.domain.profile.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookmarkDetails {
    private Long bookmarkId;

    private Long userId;

    private Long contentsId;
    private String contentsTitle;           // 작품 제목
    private String contentsPosterPath;      // 작품 포스터 이미지 경로

    private LocalDateTime createdDate;

    public static BookmarkDetails toDTO(ContentsBookmark bookmark) {
        return BookmarkDetails.builder()
                .bookmarkId(bookmark.getId())
                .userId(bookmark.getUserAccount().getId())

                .contentsId(bookmark.getContents().getContentsId())
                .contentsTitle(bookmark.getContents().getTranslatedTitle())
                .contentsPosterPath(bookmark.getContents().getPosterPath())

                .createdDate(bookmark.getCreatedDate())
                .build();
    }
}
