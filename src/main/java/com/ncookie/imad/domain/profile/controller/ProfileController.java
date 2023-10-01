package com.ncookie.imad.domain.profile.controller;

import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.profile.service.ProfileService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("")
    public ApiResponse<?> getProfileInfo() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.PROFILE_GET_INFO_SUCCESS);
    }

    @Description("북마크한 작품 목록 조회")
    @GetMapping("/bookmark/list")
    public ApiResponse<Page<ContentsBookmark>> getProfileBookmarks(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("page") int pageNumber
    ) {
        Page<ContentsBookmark> contentsBookmarkList = profileService.getContentsBookmarkList(accessToken, pageNumber);
        return ApiResponse.createSuccess(ResponseCode.PROFILE_GET_INFO_SUCCESS, contentsBookmarkList);

    }

    @Description("작품 북마크 추가")
    @PostMapping("/bookmark")
    public ApiResponse<?> addContentsBookmark(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody Map<String, Long> contentsId
    ) {
        return ApiResponse.createSuccessWithNoContent(
                profileService.addContentsBookmark(accessToken, contentsId.get("contents_id"))
        );
    }

    @Description("작품 북마크 삭제")
    @DeleteMapping("/bookmark/{bookmarkId}")
    public ApiResponse<?> deleteContentsBookmark(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long bookmarkId
    ) {
        profileService.deleteContentsBookmark(accessToken, bookmarkId);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.BOOKMARK_DELETE_SUCCESS);
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
