package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.TodayPopularReview;
import com.ncookie.imad.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodayPopularReviewsRepository extends JpaRepository<TodayPopularReview, Review> {
    Optional<TodayPopularReview> findByReview(Review review);
}
