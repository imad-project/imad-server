package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.ranking.entity.TodayPopularReview;
import com.ncookie.imad.domain.ranking.repository.TodayPopularReviewsRepository;
import com.ncookie.imad.domain.review.dto.response.ReviewDetailsResponse;
import com.ncookie.imad.domain.review.service.ReviewService;
import com.ncookie.imad.global.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TodayPopularReviewService {
    private final ReviewService reviewService;

    private final TodayPopularReviewsRepository todayPopularReviewsRepository;

    public ReviewDetailsResponse getTodayPopularReview(String accessToken) {
        List<TodayPopularReview> popularReviewList = todayPopularReviewsRepository.findTopByPopularScore();
        ReviewDetailsResponse todayPopularReview;

        if (popularReviewList.isEmpty()) {
            // 인기 리뷰 데이터가 존재하지 않으면 좋아요를 가장 많이 받은 리뷰 반환
            log.info("오늘의 리뷰 데이터가 존재하지 않아 ");
            todayPopularReview = reviewService.getMostLikeReview(accessToken);
        } else if (popularReviewList.size() > 1) {
            // 인기 리뷰 점수가 가장 높은 리뷰가 2개 이상일 때 랜덤으로 반환
            int randomNum = Utils.getRandomNum(popularReviewList.size());

            log.info("인기 점수가 가장 높은 리뷰가 2개 이상이므로 이 중 랜덤으로 반환합니다");
            todayPopularReview = reviewService.getReview(accessToken, popularReviewList.get(randomNum).getReview());
        } else {
            todayPopularReview = reviewService.getReview(accessToken, popularReviewList.get(0).getReview());
        }

        log.info("오늘의 리뷰 데이터를 반환합니다");
        return todayPopularReview;
    }
}
