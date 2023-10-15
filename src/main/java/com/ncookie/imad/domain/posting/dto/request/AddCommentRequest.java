package com.ncookie.imad.domain.posting.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddCommentRequest {
    private Long postingId;         // 게시글 ID

    private Long parentId;          // 부모 댓글 ID. 답글이 아니라면 null 값이 들어감
    private String content;         // 댓글 내용
}
