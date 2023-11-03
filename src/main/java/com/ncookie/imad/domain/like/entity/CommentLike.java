package com.ncookie.imad.domain.like.entity;

import com.ncookie.imad.domain.posting.entity.Comment;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommentLike  extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "comment_id")
    @ToString.Exclude
    private Comment comment;


    @Setter
    // 좋아요이면 +1, 싫어요이면 -1, 아무 상태도 아니면 해당 데이터 삭제
    private int likeStatus;
}
