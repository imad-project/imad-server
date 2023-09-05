package com.ncookie.imad.domain.review.controller;

import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @GetMapping("/{id}")
    public ApiResponse<?> reviewDetails(@PathVariable("id") Long id) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_GET_DETAILS_SUCCESS);
    }

    @PostMapping("/{id}")
    public ApiResponse<?> reviewAdd(@PathVariable("id") Long id) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.REVIEW_POST_DETAILS_SUCCESS);
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
