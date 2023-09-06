package com.ncookie.imad.domain.review.controller;

import com.ncookie.imad.domain.review.dto.AddReviewRequest;
import com.ncookie.imad.domain.review.dto.AddReviewResponse;
import com.ncookie.imad.domain.review.service.ReviewService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ApiResponse<?> reviewDetails(@PathVariable("id") Long id) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_GET_DETAILS_SUCCESS);
    }

    @PostMapping("")
    public ApiResponse<AddReviewResponse> reviewAdd(@RequestHeader("Authorization") String accessToken,
                                                    @RequestBody AddReviewRequest addReviewRequest) {
        return ApiResponse.createSuccess(ResponseCode.REVIEW_POST_DETAILS_SUCCESS, reviewService.addReview(accessToken, addReviewRequest));
    }

    @PatchMapping("/{id}")
    public ApiResponse<?> reviewModify(@PathVariable("id") Long id) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_PATCH_DETAILS_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> reviewDelete(@PathVariable("id") Long id) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_DELETE_DETAILS_SUCCESS);
    }

    @GetMapping("/list/{contentsId}")
    public ApiResponse<?> reviewList(@PathVariable("contentsId") Long contentsId) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_GET_LIST_SUCCESS);
    }
}
