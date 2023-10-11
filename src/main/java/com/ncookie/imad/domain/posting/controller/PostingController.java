package com.ncookie.imad.domain.posting.controller;

import com.ncookie.imad.domain.posting.dto.AddPostingRequest;
import com.ncookie.imad.domain.posting.dto.ModifyPostingRequest;
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
        return ApiResponse.createSuccess(ResponseCode.POSTING_GET_DETAILS_SUCCESS,
                postingService.getPosting(id));
    }

    @PostMapping("")
    public ApiResponse<Long> postingAdd(@RequestHeader("Authorization") String accessToken,
                                                      @RequestBody AddPostingRequest addPostingRequest) {

        return ApiResponse.createSuccess(ResponseCode.POSTING_ADD_DETAILS_SUCCESS,
                postingService.addPosting(accessToken, addPostingRequest));
    }

    @PatchMapping("/{id}")
    public ApiResponse<?> postingModify(@RequestHeader("Authorization") String accessToken,
                                        @PathVariable("id") Long id,
                                        @RequestBody ModifyPostingRequest modifyPostingRequest) {
        return ApiResponse.createSuccess(ResponseCode.POSTING_MODIFY_DETAILS_SUCCESS,
                postingService.modifyPosting(accessToken, id, modifyPostingRequest));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> postingDetails(@RequestHeader("Authorization") String accessToken,
                                         @PathVariable Long id) {
        postingService.deletePosting(accessToken, id);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.POSTING_DELETE_DETAILS_SUCCESS);
    }

    @GetMapping("/list")
    public ApiResponse<?> postingListByContents(@RequestParam(value = "contents_id") Long contentsId,
                                                @RequestParam(value = "page") int page,
                                                @RequestParam(value = "sort") String sortString,
                                                @RequestParam(value = "order") int order) {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.POSTING_GET_DETAILS_SUCCESS);
    }
}
