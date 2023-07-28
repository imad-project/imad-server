package com.ncookie.imad.domain.review.entity;

import com.ncookie.imad.domain.user.entity.UserAccount;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewLikeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;


    @Setter
    // 추천이면 +1, 비추천이면 -1, 아무 상태도 아니면 해당 데이터 삭제
    private int likeStatus;
}
