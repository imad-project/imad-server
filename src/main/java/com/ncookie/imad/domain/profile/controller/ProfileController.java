package com.ncookie.imad.domain.profile.controller;

import com.ncookie.imad.domain.posting.dto.response.PostingListResponse;
import com.ncookie.imad.domain.profile.dto.response.BookmarkListResponse;
import com.ncookie.imad.domain.profile.dto.response.ScrapListResponse;
import com.ncookie.imad.domain.profile.service.ProfileService;
import com.ncookie.imad.domain.review.dto.response.ReviewListResponse;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    @Description("프로필 관련 메인 데이터")
    @GetMapping("")
    public ApiResponse<?> getProfileInfo() {
        return ApiResponse.createSuccessWithNoContent(ResponseCode.PROFILE_GET_INFO_SUCCESS);
    }

    
    /*
     * =================================================
     * 북마크 관련
     * =================================================
     */
    @Description("북마크한 작품 목록 조회")
    @GetMapping("/bookmark/list")
    public ApiResponse<BookmarkListResponse> getProfileBookmarks(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("page") int pageNumber
    ) {
        BookmarkListResponse contentsBookmarkList = profileService.getContentsBookmarkList(accessToken, pageNumber - 1);
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
    
    
    /*
     * =================================================
     * 스크랩 관련
     * =================================================
     */
    @Description("스크랩한 게시글 목록 조회")
    @GetMapping("/scrap/list")
    public ApiResponse<ScrapListResponse> getProfileScraps(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("page") int pageNumber
    ) {
        ScrapListResponse scrapListResponse = profileService.getPostingScrapList(accessToken, pageNumber - 1);
        return ApiResponse.createSuccess(ResponseCode.PROFILE_GET_INFO_SUCCESS, scrapListResponse);
    }

    @Description("게시글 스크랩 추가")
    @PostMapping("/scrap")
    public ApiResponse<?> addPostingScrap(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody Map<String, Long> postingId
    ) {
        return ApiResponse.createSuccessWithNoContent(
                profileService.addPostingScrap(accessToken, postingId.get("posting_id"))
        );
    }

    @Description("게시글 스크랩 삭제")
    @DeleteMapping("/scrap/{scrapId}")
    public ApiResponse<?> deletePostingScrap(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long scrapId
    ) {
        profileService.deletePostingScrap(accessToken, scrapId);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.SCRAP_DELETE_SUCCESS);
    }


    /*
     * =================================================
     * 작성 및 좋아요/싫어요 표시한 리뷰/게시글
     * =================================================
     */
    @Description("작성한 게시글 목록 조회")
    @GetMapping("/posting/list")
    public ApiResponse<PostingListResponse> getProfilePostings(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("page") int pageNumber
    ) {
        return ApiResponse.createSuccess(
                ResponseCode.PROFILE_GET_WRITTEN_POSTING_LIST_SUCCESS,
                profileService.getPostingList(accessToken, pageNumber - 1));
    }

    @Description("작성한 리뷰 목록 조회")
    @GetMapping("/review/list")
    public ApiResponse<ReviewListResponse> getProfileReviews(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("page") int pageNumber
    ) {
        return ApiResponse.createSuccess(
                ResponseCode.PROFILE_GET_WRITTEN_REVIEW_LIST_SUCCESS,
                profileService.getReviewList(accessToken, pageNumber - 1)
        );
    }

    @Description("좋아요/싫어요 등록한 게시글 목록 조회")
    @GetMapping("/like/posting/list")
    public ApiResponse<PostingListResponse> getProfileLikedPostings(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("page") int pageNumber
    ) {
        return ApiResponse.createSuccess(
                ResponseCode.PROFILE_GET_LIKED_POSTING_LIST_SUCCESS,
                profileService.getLikedPostingList(accessToken, pageNumber - 1));
    }

    @Description("좋아요/싫어요 등록한 리뷰 목록 조회")
    @GetMapping("/like/review/list")
    public ApiResponse<ReviewListResponse> getProfileLikedReviews(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("page") int pageNumber
    ) {
        return ApiResponse.createSuccess(
                ResponseCode.PROFILE_GET_LIKED_REVIEW_LIST_SUCCESS,
                profileService.getLikedReviewList(accessToken, pageNumber - 1)
        );
    }
}
