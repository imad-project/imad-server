package com.ncookie.imad.domain.posting.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddPostingRequest {
    private Long contentsId;            // 작품 ID

    private String title;               // 게시글 제목
    private String content;             // 게시글  본문
    private int category;               // 게시글 카테고리

    private boolean isSpoiler;          // 스포일러 여부
}
