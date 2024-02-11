package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.ranking.entity.TodayPopularReview;
import com.ncookie.imad.domain.ranking.repository.TodayPopularReviewsRepository;
import com.ncookie.imad.domain.review.dto.response.ReviewDetailsResponse;
import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.global.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TodayPopularReviewService {
    private final TodayPopularReviewsRepository todayPopularReviewsRepository;

    public final static int POPULAR_LIKE_SCORE = 5;

    public ReviewDetailsResponse getTodayPopularReview() {
        List<TodayPopularReview> popularReviewList = todayPopularReviewsRepository.findTopByPopularScore();
        // 인기 리뷰 데이터가 존재하지 않으면 null 반환
        if (popularReviewList.isEmpty()) {
            return null;
        }

        // 인기 리뷰 점수가 가장 높은 리뷰가 2개 이상일 때 랜덤으로 반환
        if (popularReviewList.size() > 1) {
            int randomNum = Utils.getRandomNum(popularReviewList.size());
            return ReviewDetailsResponse.toDTO(popularReviewList.get(randomNum).getReview());
        }
        return ReviewDetailsResponse.toDTO(popularReviewList.get(0).getReview());
    }

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
