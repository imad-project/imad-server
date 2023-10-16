package com.ncookie.imad.domain.review.controller;

import com.ncookie.imad.domain.review.dto.request.AddReviewRequest;
import com.ncookie.imad.domain.review.dto.request.ModifyReviewRequest;
import com.ncookie.imad.domain.like.dto.LikeStatusRequest;
import com.ncookie.imad.domain.review.dto.response.AddReviewResponse;
import com.ncookie.imad.domain.review.dto.response.ReviewDetailsResponse;
import com.ncookie.imad.domain.review.dto.response.ReviewListResponse;
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
    public ApiResponse<ReviewDetailsResponse> reviewDetails(@RequestHeader("Authorization") String accessToken,
                                                            @PathVariable("id") Long id) {
        return ApiResponse.createSuccess(ResponseCode.REVIEW_GET_DETAILS_SUCCESS, reviewService.getReview(accessToken, id));
    }

    @GetMapping("/list")
    public ApiResponse<ReviewListResponse> reviewList(@RequestHeader("Authorization") String accessToken,
                                                      @RequestParam(value = "contents_id") Long contentsId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "sort") String sortString,
                                                      @RequestParam(value = "order") int order) {
        ReviewListResponse reviewList = reviewService.getReviewList(accessToken, contentsId, page - 1, sortString, order);
        return ApiResponse.createSuccess(ResponseCode.REVIEW_GET_LIST_SUCCESS, reviewList);
    }

    @PostMapping("")
    public ApiResponse<AddReviewResponse> reviewAdd(@RequestHeader("Authorization") String accessToken,
                                                    @RequestBody AddReviewRequest addReviewRequest) {
        return ApiResponse.createSuccess(ResponseCode.REVIEW_ADD_DETAILS_SUCCESS, reviewService.addReview(accessToken, addReviewRequest));
    }

    @PatchMapping("/{id}")
    public ApiResponse<Long> reviewModify(@RequestHeader("Authorization") String accessToken,
                                          @PathVariable("id") Long id,
                                          @RequestBody ModifyReviewRequest modifyReviewRequest) {
        return ApiResponse.createSuccess(
                ResponseCode.REVIEW_MODIFY_DETAILS_SUCCESS,
                reviewService.modifyReview(accessToken, id, modifyReviewRequest)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> reviewDelete(@RequestHeader("Authorization") String accessToken,
                                       @PathVariable("id") Long id) {
        reviewService.deleteReview(accessToken, id);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_DELETE_DETAILS_SUCCESS);
    }

    @PatchMapping("/like/{id}")
    public ApiResponse<?> reviewLikeStatusModify(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable("id") Long id,
                                                 @RequestBody LikeStatusRequest likeStatusRequest) {
        reviewService.saveLikeStatus(accessToken, id, likeStatusRequest.getLikeStatus());
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_LIKE_STATUS_MODIFY_SUCCESS);
    }
}
