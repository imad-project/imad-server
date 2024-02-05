package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.TodayPopularReview;
import com.ncookie.imad.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodayPopularReviewsRepository extends JpaRepository<TodayPopularReview, Review> {
}