package com.ncookie.imad.domain.posting.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ModifyPostingRequest {
    private String title;               // 게시글 제목
    private String content;             // 게시글 본문
    private int category;

    @JsonProperty("is_spoiler")
    private boolean isSpoiler;          // 스포일러 여부
}
