package com.ncookie.imad.domain.posting.controller;

import com.ncookie.imad.domain.posting.dto.*;
import com.ncookie.imad.domain.posting.service.PostingService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posting")
public class PostingController {
    private final PostingService postingService;

    @Description("게시글 조회")
    @GetMapping("/{id}")
    public ApiResponse<PostingDetailsResponse> postingDetails(@PathVariable Long id) {
        return ApiResponse.createSuccess(ResponseCode.POSTING_GET_DETAILS_SUCCESS,
                postingService.getPosting(id));
    }

    @Description("게시글 등록")
    @PostMapping("")
    public ApiResponse<PostingIdResponse> postingAdd(@RequestHeader("Authorization") String accessToken,
                                                      @RequestBody AddPostingRequest addPostingRequest) {

        return ApiResponse.createSuccess(ResponseCode.POSTING_ADD_DETAILS_SUCCESS,
                postingService.addPosting(accessToken, addPostingRequest));
    }

    @Description("게시글 수정")
    @PatchMapping("/{id}")
    public ApiResponse<PostingIdResponse> postingModify(@RequestHeader("Authorization") String accessToken,
                                                        @PathVariable("id") Long id,
                                                        @RequestBody ModifyPostingRequest modifyPostingRequest) {
        return ApiResponse.createSuccess(ResponseCode.POSTING_MODIFY_DETAILS_SUCCESS,
                postingService.modifyPosting(accessToken, id, modifyPostingRequest));
    }

    @Description("게시글 삭제")
    @DeleteMapping("/{id}")
    public ApiResponse<?> postingDelete(@RequestHeader("Authorization") String accessToken,
                                         @PathVariable Long id) {
        postingService.deletePosting(accessToken, id);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.POSTING_DELETE_DETAILS_SUCCESS);
    }

    @Description("게시글 리스트 전체 조회")
    @GetMapping("/list")
    public ApiResponse<PostingListResponse> postingList(@RequestHeader("Authorization") String accessToken,
                                                        @RequestParam(value = "page") int page) {
        return ApiResponse.createSuccess(ResponseCode.POSTING_GET_LIST_SUCCESS, postingService.getAllPostingList(accessToken, page));
    }

    @Description("게시글 리스트 조건부 조회")
    @GetMapping("/list/search")
    public ApiResponse<PostingListResponse> postingListByQuery(@RequestHeader("Authorization") String accessToken,
                                                @RequestParam(value = "search_type") int searchType,
                                                @RequestParam(value = "query") String query,
                                                @RequestParam(value = "page") int page,
                                                @RequestParam(value = "sort") String sortString,
                                                @RequestParam(value = "order") int order) {

        return ApiResponse.createSuccess(
                ResponseCode.POSTING_GET_LIST_SUCCESS,
                postingService.getAllPostingListByQuery(accessToken, searchType, query, page, sortString, order)
        );
    }
}
