package com.ncookie.imad.domain.ranking.entity;

import com.ncookie.imad.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TodayPopularReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "review_id")
    @ToString.Exclude
    private Review review;

    // 인기 리뷰 점수
    @Setter
    private Long popularScore;
}
