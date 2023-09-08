package com.ncookie.imad.domain.review_like.repository;

import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.review_like.entity.ReviewLike;
import com.ncookie.imad.domain.user.entity.UserAccount;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    ReviewLike findByUserAccountAndReview(UserAccount user, Review review);

    @Query("SELECT COUNT(*) FROM ReviewLike WHERE review = :review and likeStatus = 1")
    int countLikeByReview(@Param("review") Review review);

    @Query("SELECT COUNT(*) FROM ReviewLike WHERE review = :review and likeStatus = -1")
    int countDislikeByReview(@Param("review") Review review);
}
