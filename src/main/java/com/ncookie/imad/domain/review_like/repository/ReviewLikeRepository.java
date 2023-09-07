package com.ncookie.imad.domain.review_like.repository;

import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.review_like.entity.ReviewLike;
import com.ncookie.imad.domain.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    ReviewLike findByUserAccountAndReview(UserAccount user, Review review);
}