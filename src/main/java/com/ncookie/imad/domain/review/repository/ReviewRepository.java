package com.ncookie.imad.domain.review.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(score) FROM Review WHERE contents = :contents")
    Float calculateAverageScoreByReview(Contents contents);
}
