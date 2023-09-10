package com.ncookie.imad.domain.review_like.service;


import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.review_like.entity.ReviewLike;
import com.ncookie.imad.domain.review_like.repository.ReviewLikeRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewLikeService {
    private final ReviewLikeRepository reviewLikeRepository;

    public ReviewLike findByUserAccountAndReview(UserAccount user, Review review) {
        return reviewLikeRepository.findByUserAccountAndReview(user, review);
    }

    public ReviewLike saveReviewLikeStatus(ReviewLike reviewLike) {
        return reviewLikeRepository.save(reviewLike);
    }

    public void deleteReviewLike(ReviewLike reviewLike) {
        reviewLikeRepository.delete(reviewLike);
    }

    public int getLikeCount(Review review) {
        return reviewLikeRepository.countLikeByReview(review);
    }

    public int getDislikeCount(Review review) {
        return reviewLikeRepository.countDislikeByReview(review);
    }
}
