package com.ncookie.imad.domain.profile.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.contents.entity.ContentsType;
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
    private Long contentsTmdbId;            // 작품 TMDB ID
    private String contentsTitle;           // 작품 제목
    private String contentsPosterPath;      // 작품 포스터 이미지 경로
    private ContentsType contentsType;      // 작품 타입

    private LocalDateTime createdDate;

    public static BookmarkDetails toDTO(ContentsBookmark bookmark) {
        return BookmarkDetails.builder()
                .bookmarkId(bookmark.getId())
                .userId(bookmark.getUserAccount().getId())

                .contentsId(bookmark.getContents().getContentsId())
                .contentsTmdbId(bookmark.getContents().getTmdbId())
                .contentsTitle(bookmark.getContents().getTranslatedTitle())
                .contentsPosterPath(bookmark.getContents().getPosterPath())
                .contentsType(bookmark.getContents().getContentsType())

                .createdDate(bookmark.getCreatedDate())
                .build();
    }
}
