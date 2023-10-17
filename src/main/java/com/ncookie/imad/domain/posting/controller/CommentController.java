package com.ncookie.imad.domain.posting.controller;

import com.ncookie.imad.domain.posting.dto.request.AddCommentRequest;
import com.ncookie.imad.domain.posting.dto.request.ModifyCommentRequest;
import com.ncookie.imad.domain.posting.dto.response.CommentIdResponse;
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


    @Description("댓글 등록")
    @PostMapping("")
    public ApiResponse<CommentIdResponse> commentAdd(@RequestHeader("Authorization") String accessToken,
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
}