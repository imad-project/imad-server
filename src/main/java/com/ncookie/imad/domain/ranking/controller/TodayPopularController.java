package com.ncookie.imad.domain.ranking.controller;

import com.ncookie.imad.domain.ranking.service.TodayPopularPostingService;
import com.ncookie.imad.domain.ranking.service.TodayPopularReviewService;
import com.ncookie.imad.domain.review.dto.response.ReviewDetailsResponse;
import com.ncookie.imad.domain.review.service.ReviewService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/popular")
public class TodayPopularController {
    private final TodayPopularPostingService todayPopularPostingService;
    private final TodayPopularReviewService todayPopularReviewService;
    
    private final ReviewService reviewService;

    @Description("오늘의 리뷰 조회")
    @GetMapping("/review")
    public ApiResponse<ReviewDetailsResponse> rankingGetWeekly() {
        ReviewDetailsResponse todayReview = todayPopularReviewService.getTodayPopularReview();

        // 현재 인기 리뷰 데이터가 없는 상태이므로 좋아요가 가장 많은 리뷰 데이터 반환
        if (todayReview == null) {
            log.info("인기 리뷰 데이터가 없으므로 좋아요가 가장 많은 리뷰를 조회합니다...");
            ReviewDetailsResponse popularReview = reviewService.getMostLikeReview();

            // 리뷰 좋아요 데이터도 없는 경우
            if (popularReview == null) {
                log.info("리뷰의 좋아요 데이터도 존재하지 않으므로 이에 맞는 응답을 반환합니다.");
                return ApiResponse.createSuccess(ResponseCode.POPULAR_REVIEW_ALL_NULL, null);
            }
            return ApiResponse.createSuccess(ResponseCode.POPULAR_REVIEW_NULL_AND_GET_REVIEW, popularReview);
        }

        // 오늘의 리뷰 데이터 반환
        return ApiResponse.createSuccess(ResponseCode.POPULAR_REVIEW_GET_SUCCESS, todayReview);
    }

//    @Description("인기 게시글 조회")
//    @GetMapping("/posting")
//    public ApiResponse<RankingListResponse> rankingGetWeekly(
//            @RequestParam(value = "page") int pageNumber,
//            @RequestParam(value = "type") String contentsTypeString) {
//        RankingListResponse rankingList = rankingSystemService.g
//        etRankingList(RankingPeriod.WEEKLY, contentsTypeString, pageNumber - 1);
//        if (rankingList == null) {
//            return ApiResponse.createSuccess(ResponseCode.RANKING_NOTICE_RANKING_UPDATE_TIME, null);
//        }
//
//        return ApiResponse.createSuccess(ResponseCode.RANKING_GET_SUCCESS, rankingList);
//    }
}
