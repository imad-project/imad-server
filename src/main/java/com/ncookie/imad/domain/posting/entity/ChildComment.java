package com.ncookie.imad.domain.posting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChildComment extends Comment {
    // 부모 댓글 id
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment parentComment;
}
