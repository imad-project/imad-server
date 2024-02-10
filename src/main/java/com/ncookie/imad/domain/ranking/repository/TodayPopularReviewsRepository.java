package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.TodayPopularReview;
import com.ncookie.imad.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TodayPopularReviewsRepository extends JpaRepository<TodayPopularReview, Review> {
    Optional<TodayPopularReview> findByReview(Review review);

    @Query("SELECT pr FROM TodayPopularReview pr " +
            "WHERE pr.popularScore = (SELECT MAX(pr2.popularScore) FROM TodayPopularReview pr2)")
    List<TodayPopularReview> findTopByPopularScore();
}
