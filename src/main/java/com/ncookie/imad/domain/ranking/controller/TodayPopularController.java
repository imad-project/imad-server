package com.ncookie.imad.domain.ranking.controller;

import com.ncookie.imad.domain.posting.dto.response.PostingDetailsResponse;
import com.ncookie.imad.domain.posting.dto.response.PostingListElement;
import com.ncookie.imad.domain.posting.service.PostingService;
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
    private final TodayPopularReviewService todayPopularReviewService;
    private final TodayPopularPostingService todayPopularPostingService;

    private final ReviewService reviewService;
    private final PostingService postingService;


    @Description("오늘의 리뷰 조회")
    @GetMapping("/review")
    public ApiResponse<ReviewDetailsResponse> getPopularReview() {
        ReviewDetailsResponse todayPopularReview = todayPopularReviewService.getTodayPopularReview();

        // 현재 인기 리뷰 데이터가 없는 상태이므로 좋아요가 가장 많은 리뷰 데이터 반환
        if (todayPopularReview == null) {
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
        return ApiResponse.createSuccess(ResponseCode.POPULAR_REVIEW_GET_SUCCESS, todayPopularReview);
    }

    @Description("인기 게시글 조회")
    @GetMapping("/posting")
    public ApiResponse<PostingListElement> getPopularPosting() {
        PostingListElement todayPopularPosting = todayPopularPostingService.getTodayPopularPosting();

        // 현재 인기 게시글 데이터가 없는 상태이므로 좋아요가 가장 많은 게시글 데이터 반환
        if (todayPopularPosting == null) {
            log.info("인기 리뷰 데이터가 없으므로 좋아요가 가장 많은 리뷰를 조회합니다...");
            PostingListElement mostLikePosting = postingService.getMostLikePosting();

            // 리뷰 좋아요 데이터도 없는 경우
            if (mostLikePosting == null) {
                log.info("리뷰의 좋아요 데이터도 존재하지 않으므로 이에 맞는 응답을 반환합니다.");
                return ApiResponse.createSuccess(ResponseCode.POPULAR_REVIEW_ALL_NULL, null);
            }
            return ApiResponse.createSuccess(ResponseCode.POPULAR_REVIEW_NULL_AND_GET_REVIEW, mostLikePosting);
        }

        // 오늘의 리뷰 데이터 반환
        return ApiResponse.createSuccess(ResponseCode.POPULAR_REVIEW_GET_SUCCESS, todayPopularPosting);
    }
}
