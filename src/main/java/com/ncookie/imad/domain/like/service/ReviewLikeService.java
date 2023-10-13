package com.ncookie.imad.domain.like.service;


import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.like.entity.ReviewLike;
import com.ncookie.imad.domain.like.repository.ReviewLikeRepository;
import com.ncookie.imad.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewLikeService implements LikeService<Review, ReviewLike> {
    private final ReviewLikeRepository reviewLikeRepository;

    @Override
    public ReviewLike findByUserAccountAndE(UserAccount user, Review review) {
        return reviewLikeRepository.findByUserAccountAndReview(user, review);
    }

    @Override
    public ReviewLike saveLikeStatus(ReviewLike reviewLike) {
        return reviewLikeRepository.save(reviewLike);
    }

    @Override
    public void deleteLikeStatus(ReviewLike reviewLike) {
        reviewLikeRepository.delete(reviewLike);
    }

    @Override
    public int getLikeCount(Review review) {
        return reviewLikeRepository.countLikeByReview(review);
    }

    @Override
    public int getDislikeCount(Review review) {
        return reviewLikeRepository.countDislikeByReview(review);
    }

    @Override
    public Page<ReviewLike> getLikedListByUser(UserAccount user, Pageable pageable) {
        return reviewLikeRepository.findAllByUserAccount(user, pageable);
    }
}
