package com.ncookie.imad.domain.posting.controller;

import com.ncookie.imad.domain.like.dto.LikeStatusRequest;
import com.ncookie.imad.domain.posting.dto.request.AddCommentRequest;
import com.ncookie.imad.domain.posting.dto.request.ModifyCommentRequest;
import com.ncookie.imad.domain.posting.dto.response.CommentDetailsResponse;
import com.ncookie.imad.domain.posting.dto.response.CommentIdResponse;
import com.ncookie.imad.domain.posting.dto.response.CommentListResponse;
import com.ncookie.imad.domain.posting.service.CommentService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posting/comment")
public class CommentController {
    private final CommentService commentService;


    @Description("댓글 조회")
    @GetMapping("/{id}")
    public ApiResponse<CommentDetailsResponse> commentGet(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("id") Long commentId) {
        return ApiResponse.createSuccess(
                ResponseCode.COMMENT_GET_SUCCESS,
                commentService.getComment(accessToken, commentId));
    }

    @Description("댓글 리스트 조회")
    @GetMapping("/list")
    public ApiResponse<CommentListResponse> commentListGet(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("posting_id") Long postingId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "sort") String sortString,
            @RequestParam(value = "order") int order
    ) {
        return ApiResponse.createSuccess(ResponseCode.COMMENT_GET_LIST_SUCCESS,
                commentService.getCommentListByPosting(accessToken, postingId, page - 1, sortString, order));
    }

    @Description("댓글 등록")
    @PostMapping("")
    public ApiResponse<CommentIdResponse> commentAdd(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody AddCommentRequest addCommentRequest) {
        return ApiResponse.createSuccess(
                ResponseCode.COMMENT_ADD_SUCCESS,
                commentService.addComment(accessToken, addCommentRequest));
    }

    @Description("댓글 수정")
    @PatchMapping("/{id}")
    public ApiResponse<CommentIdResponse> commentModify(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("id") Long commentId,
            @RequestBody ModifyCommentRequest modifyCommentRequest) {
        return ApiResponse.createSuccess(
                ResponseCode.COMMENT_MODIFY_SUCCESS,
                commentService.modifyComment(accessToken, commentId, modifyCommentRequest));
    }

    @Description("댓글 삭제")
    @DeleteMapping("/{id}")
    public ApiResponse<CommentIdResponse> commentDelete(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("id") Long id) {
        return ApiResponse.createSuccess(
                ResponseCode.COMMENT_DELETE_SUCCESS,
                commentService.deleteComment(accessToken, id));
    }

    @Description("댓글 좋아요/싫어요")
    @PatchMapping("/like/{id}")
    public ApiResponse<?> commentLikeStatusModify(@RequestHeader("Authorization") String accessToken,
                                                  @PathVariable("id") Long id,
                                                  @RequestBody LikeStatusRequest likeStatusRequest) {
        commentService.saveLikeStatus(accessToken, id, likeStatusRequest.getLikeStatus());
        return ApiResponse.createSuccessWithNoContent(ResponseCode.COMMENT_LIKE_STATUS_MODIFY_SUCCESS);
    }
}
