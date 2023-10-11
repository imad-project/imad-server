package com.ncookie.imad.domain.posting.controller;

import com.ncookie.imad.domain.posting.service.PostingService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posting")
public class PostingController {
    private final PostingService postingService;

    @GetMapping("/{id}")
    public ApiResponse<?> postingDetails(@PathVariable Long id) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.POSTING_GET_DETAILS_SUCCESS);
    }

    @PostMapping("")
    public ApiResponse<?> postingAdd() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.POSTING_GET_DETAILS_SUCCESS);
    }

    @PatchMapping("/{id}")
    public ApiResponse<?> postingModify(@RequestHeader("Authorization") String accessToken,
                                        @PathVariable("id") Long id) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.POSTING_GET_DETAILS_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> postingDetails(@RequestHeader("Authorization") String accessToken,
                                         @PathVariable Long id) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.POSTING_GET_DETAILS_SUCCESS);
    }

    @GetMapping("/list")
    public ApiResponse<?> postingListByContents(@RequestParam(value = "contents_id") Long contentsId,
                                                @RequestParam(value = "page") int page,
                                                @RequestParam(value = "sort") String sortString,
                                                @RequestParam(value = "order") int order) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.POSTING_GET_DETAILS_SUCCESS);
    }
}
