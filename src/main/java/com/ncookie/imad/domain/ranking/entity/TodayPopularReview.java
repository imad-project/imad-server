package com.ncookie.imad.domain.ranking.entity;

import com.ncookie.imad.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TodayPopularReview {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "review_id")
    @ToString.Exclude
    private Review review;

    // 인기 리뷰 점수
    private Long popularScore;
}
