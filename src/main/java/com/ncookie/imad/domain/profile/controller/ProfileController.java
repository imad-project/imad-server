package com.ncookie.imad.domain.profile.controller;

import com.ncookie.imad.domain.profile.service.ProfileService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("")
    public ApiResponse<?> getProfileInfo() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.PROFILE_GET_INFO_SUCCESS);
    }

    @Description("찜한 작품 목록 조회")
    @GetMapping("/bookmarks")
    public ApiResponse<?> getProfileBookmarks() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.PROFILE_GET_INFO_SUCCESS);
    }

    @Description("스크랩한 게시글 목록 조회")
    @GetMapping("/scraps")
    public ApiResponse<?> getProfileScraps() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.PROFILE_GET_INFO_SUCCESS);
    }

    @Description("작성한 게시글 목록 조회")
    @GetMapping("/postings")
    public ApiResponse<?> getProfilePostings() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.PROFILE_GET_INFO_SUCCESS);
    }

    @Description("작성한 리뷰 목록 조회")
    @GetMapping("/reviews")
    public ApiResponse<?> getProfileReviews() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.PROFILE_GET_INFO_SUCCESS);
    }

    @Description("좋아요한 게시글 목록 조회")
    @GetMapping("/like/postings")
    public ApiResponse<?> getProfileLikedPostings() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.PROFILE_GET_INFO_SUCCESS);
    }

    @Description("좋아요한 리뷰 목록 조회")
    @GetMapping("/like/reviews")
    public ApiResponse<?> getProfileLikedReviews() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.PROFILE_GET_INFO_SUCCESS);
    }
}
