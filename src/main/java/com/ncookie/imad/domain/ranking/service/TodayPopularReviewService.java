package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.ranking.entity.TodayPopularReview;
import com.ncookie.imad.domain.ranking.repository.TodayPopularReviewsRepository;
import com.ncookie.imad.domain.review.entity.Review;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TodayPopularReviewService {
    private final TodayPopularReviewsRepository todayPopularReviewsRepository;

    public final static int POPULAR_LIKE_SCORE = 5;

    public void addPopularScore(Review review, int score) {
        if (review == null) {
            log.warn("인기 점수 반영 실패 : ID에 해당하는 리뷰가 존재하지 않음");
            return;
        }

        Optional<TodayPopularReview> popularReviewOptional = todayPopularReviewsRepository.findByReview(review);
        if (popularReviewOptional.isPresent()) {
            TodayPopularReview todayPopularReview = popularReviewOptional.get();
            Long oldScore = todayPopularReview.getPopularScore();

            todayPopularReview.setPopularScore(oldScore + score);
            todayPopularReviewsRepository.save(todayPopularReview);
        } else {
            todayPopularReviewsRepository.save(
                    TodayPopularReview.builder()
                            .review(review)
                            .popularScore((long) score)
                            .build()
            );
        }
        
        log.info("리뷰 인기 점수 추가 완료");
    }

    public void subtractPopularScore(Review review, int score) {
        if (review == null) {
            log.warn("인기 점수 반영 실패 : ID에 해당하는 리뷰가 존재하지 않음");
            return;
        }

        Optional<TodayPopularReview> popularReviewOptional = todayPopularReviewsRepository.findByReview(review);
        if (popularReviewOptional.isPresent()) {
            TodayPopularReview todayPopularReview = popularReviewOptional.get();
            Long oldScore = todayPopularReview.getPopularScore();

            todayPopularReview.setPopularScore(oldScore - score);
            todayPopularReviewsRepository.save(todayPopularReview);
        } else {
            todayPopularReviewsRepository.save(
                    TodayPopularReview.builder()
                            .review(review)
                            .popularScore((long) score)
                            .build()
            );
        }

        log.info("리뷰 인기 점수 차감 완료");
    }
}
