package com.ncookie.imad.domain.ranking.controller;

import com.ncookie.imad.domain.posting.dto.response.PostingDetailsResponse;
import com.ncookie.imad.domain.ranking.service.TodayPopularPostingService;
import com.ncookie.imad.domain.ranking.service.TodayPopularReviewService;
import com.ncookie.imad.domain.review.dto.response.ReviewDetailsResponse;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/popular")
public class TodayPopularController {

    private final TodayPopularReviewService todayPopularReviewService;
    private final TodayPopularPostingService todayPopularPostingService;


    @Description("오늘의 리뷰 조회")
    @GetMapping("/review")
    public ApiResponse<ReviewDetailsResponse> getPopularReview(@RequestHeader("Authorization") String accessToken) {
        ReviewDetailsResponse todayPopularReview = todayPopularReviewService.getTodayPopularReview(accessToken);
        return ApiResponse.createSuccess(ResponseCode.POPULAR_REVIEW_GET_SUCCESS, todayPopularReview);
    }

    @Description("인기 게시글 조회")
    @GetMapping("/posting")
    public ApiResponse<PostingDetailsResponse> getPopularPosting(@RequestHeader("Authorization") String accessToken) {
        PostingDetailsResponse todayPopularPosting = todayPopularPostingService.getTodayPopularPosting(accessToken);
        return ApiResponse.createSuccess(ResponseCode.POPULAR_POSTING_GET_SUCCESS, todayPopularPosting);
    }
}
