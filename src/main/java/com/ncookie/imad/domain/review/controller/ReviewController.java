package com.ncookie.imad.domain.review.controller;

import com.ncookie.imad.domain.review.dto.ModifyReviewRequest;
import com.ncookie.imad.domain.review.dto.ReviewDetailsResponse;
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
    public ApiResponse<ReviewDetailsResponse> reviewDetails(@PathVariable("id") Long id) {
        return ApiResponse.createSuccess(ResponseCode.REVIEW_GET_DETAILS_SUCCESS, reviewService.getReview(id));
    }

    @PostMapping("")
    public ApiResponse<AddReviewResponse> reviewAdd(@RequestHeader("Authorization") String accessToken,
                                                    @RequestBody AddReviewRequest addReviewRequest) {
        return ApiResponse.createSuccess(ResponseCode.REVIEW_POST_DETAILS_SUCCESS, reviewService.addReview(accessToken, addReviewRequest));
    }

    @PatchMapping("/{id}")
    public ApiResponse<Long> reviewModify(@RequestHeader("Authorization") String accessToken,
                                          @PathVariable("id") Long id,
                                          @RequestBody ModifyReviewRequest modifyReviewRequest) {
        return ApiResponse.createSuccess(
                ResponseCode.REVIEW_PATCH_DETAILS_SUCCESS,
                reviewService.modifyReview(accessToken, id, modifyReviewRequest)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> reviewDelete(@RequestHeader("Authorization") String accessToken,
                                       @PathVariable("id") Long id) {
        reviewService.deleteReview(accessToken, id);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_DELETE_DETAILS_SUCCESS);
    }

    @GetMapping("/list/{contentsId}")
    public ApiResponse<?> reviewList(@PathVariable("contentsId") Long contentsId) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_GET_LIST_SUCCESS);
    }

    @PatchMapping("/review/{id}")
    public ApiResponse<?> reviewLikeStatusModify(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable("id") Long id,
                                                 @RequestBody int likeStatus) {
        reviewService.modifyLikeStatus(accessToken, id, likeStatus);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_LIKE_STATUS_MODIFY_SUCCESS);
    }
}