package com.ncookie.imad.domain.review_like.entity;

import com.ncookie.imad.domain.review.entity.Review;
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
public class ReviewLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewLikeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "review_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Review review;


    @Setter
    // 좋아요이면 +1, 싫어요이면 -1, 아무 상태도 아니면 해당 데이터 삭제
    private int likeStatus;
}
