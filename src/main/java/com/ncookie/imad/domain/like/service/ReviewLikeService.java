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
    public ReviewLike saveLikeStatus(ReviewLike like) {
        return reviewLikeRepository.save(like);
    }

    @Override
    public void deleteLikeStatus(ReviewLike like) {
        reviewLikeRepository.delete(like);
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
    public Page<ReviewLike> getLikedListByUser(UserAccount user, Pageable pageable, int likeStatus) {
        // likeStatus가 0이면 전체조회, 1이면 좋아요, -1이면 싫어요 등록한 리뷰 리스트를 조회함
        if (likeStatus == 0) {
            return reviewLikeRepository.findAllByUserAccount(user, pageable);
        } else {
            return reviewLikeRepository.findAllByUserAccountAndLikeStatus(pageable, user, likeStatus);
        }
    }
}
