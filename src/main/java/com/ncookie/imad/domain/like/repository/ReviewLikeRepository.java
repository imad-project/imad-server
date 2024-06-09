package com.ncookie.imad.domain.like.repository;

import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.like.entity.ReviewLike;
import com.ncookie.imad.domain.user.entity.UserAccount;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    ReviewLike findByUserAccountAndReview(UserAccount user, Review review);

    Page<ReviewLike> findAllByUserAccount(UserAccount user, Pageable pageable);

    @Query("SELECT r FROM ReviewLike r WHERE r.userAccount = :user AND r.likeStatus = :likeStatus")
    Page<ReviewLike> findAllByUserAccountAndLikeStatus(Pageable pageable, @Param("user") UserAccount user, @Param("likeStatus") int likeStatus);

    @Query("SELECT COUNT(*) FROM ReviewLike WHERE review = :review AND likeStatus = 1")
    int countLikeByReview(@Param("review") Review review);

    @Query("SELECT COUNT(*) FROM ReviewLike WHERE review = :review AND likeStatus = -1")
    int countDislikeByReview(@Param("review") Review review);
}
