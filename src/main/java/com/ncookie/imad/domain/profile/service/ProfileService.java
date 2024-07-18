package com.ncookie.imad.domain.profile.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.posting.dto.response.PostingListResponse;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.posting.service.PostingRetrievalService;
import com.ncookie.imad.domain.posting.service.PostingService;
import com.ncookie.imad.domain.profile.dto.response.*;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.profile.entity.PostingScrap;
import com.ncookie.imad.domain.ranking.service.ContentsRankingScoreUpdateService;
import com.ncookie.imad.domain.ranking.service.TodayPopularScoreService;
import com.ncookie.imad.domain.review.dto.response.ReviewListResponse;
import com.ncookie.imad.domain.review.service.ReviewService;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
import com.ncookie.imad.domain.useractivity.service.UserActivityService;
import com.ncookie.imad.global.Utils;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ncookie.imad.domain.ranking.service.ContentsRankingScoreUpdateService.BOOKMARK_RANKING_SCORE;
import static com.ncookie.imad.domain.ranking.service.ContentsRankingScoreUpdateService.SCRAP_RANKING_SCORE;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProfileService {
    private final UserRetrievalService userRetrievalService;
    private final ContentsService contentsService;
    private final PostingRetrievalService postingRetrievalService;

    private final ReviewService reviewService;
    private final PostingService postingService;

    private final BookmarkService bookmarkService;
    private final ScrapService scrapService;

    // 기록용
    private final ContentsRankingScoreUpdateService contentsRankingScoreUpdateService;
    private final TodayPopularScoreService todayPopularScoreService;
    private final UserActivityService userActivityService;


    public ProfileSummaryInfoResponse getMyProfileSummary(String accessToken) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        return getProfileSummaryInfo(user);
    }

    public ProfileSummaryInfoResponse getOthersProfileSummary(Long userId) {
        UserAccount user = userRetrievalService.getUserById(userId);
        return getProfileSummaryInfo(user);
    }

    // 프로필 요약 정보
    public ProfileSummaryInfoResponse getProfileSummaryInfo(UserAccount user) {
        BookmarkListResponse contentsBookmarkList = getContentsBookmarkList(user, 0);

        return ProfileSummaryInfoResponse.builder()
                .userId(user.getId())
                .userNickname(user.getNickname())
                .userProfileImage(user.getProfileImage())

                .myReviewCnt(reviewService.getWrittenReviewCount(user))
                .myPostingCnt(postingService.getWrittenPostingCount(user))
                .myScrapCnt(scrapService.getScrapCountByUser(user))

                .bookmarkListResponse(contentsBookmarkList)

                .build();
    }

    /*
     * =================================================
     * 북마크 관련
     * =================================================
     */
    public BookmarkListResponse getContentsBookmarkListByController(String accessToken, int pageNumber) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        return getContentsBookmarkList(user, pageNumber);
    }

    public BookmarkListResponse getContentsBookmarkList(UserAccount user, int pageNumber) {
        Page<ContentsBookmark> contentsBookmarkPage = bookmarkService.findAllByUserAccount(
                user,
                PageRequest.of(pageNumber, Utils.PAGE_SIZE)
        );

        List<BookmarkDetails> bookmarkDetailsList = new ArrayList<>();
        for (ContentsBookmark bookmark : contentsBookmarkPage.getContent().stream().toList()) {
            bookmarkDetailsList.add(BookmarkDetails.toDTO(bookmark));
        }

        return BookmarkListResponse.toDTO(contentsBookmarkPage, bookmarkDetailsList);
    }
    
    // 작품 북마크 추가
    public ResponseCode addContentsBookmark(String accessToken, Long contentsId) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        Contents contents = contentsService.getContentsEntityById(contentsId);

        if (!bookmarkService.existsByUserAccountAndContents(user, contents)) {
            ContentsBookmark bookmark = bookmarkService.save(
                    ContentsBookmark.builder()
                            .userAccount(user)
                            .contents(contents)
                            .build()
            );
            log.info("북마크 등록 완료");

            // 유저 활동 기록
            userActivityService.addContentsBookmark(user, contents, bookmark);

            contentsRankingScoreUpdateService.addRankingScore(contents, BOOKMARK_RANKING_SCORE);
            log.info("[작품 북마크 추가] 랭킹 점수 반영 완료");
            
            return ResponseCode.BOOKMARK_ADD_SUCCESS;
        } else {
            // 해당 북마크가 이미 있을 때
            log.error("북마크 등록 실패 : 이미 존재하는 북마크");
            return ResponseCode.BOOKMARK_ALREADY_EXIST;
        }
    }

    // 작품 북마크 삭제
    public void deleteContentsBookmark(String accessToken, Long bookmarkId) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // 삭제할 데이터가 존재하지 않는 경우
        if (!bookmarkService.existsById(bookmarkId)) {
            log.error("북마크 삭제 실패 : 유효하지 않은 ID");
            throw new BadRequestException(ResponseCode.BOOKMARK_WRONG_ID);
        }
        ContentsBookmark bookmark = bookmarkService.findByIdAndUserAccount(bookmarkId, user);
        contentsRankingScoreUpdateService.subtractRankingScore(bookmark.getContents(), BOOKMARK_RANKING_SCORE);
        log.info("[작품 북마크 삭제] 랭킹 점수 반영 완료");

        userActivityService.removeContentsBookmark(bookmark);

        bookmarkService.deleteByIdAndUserAccount(bookmarkId, user);
        log.info("북마크 삭제 완료");
    }


    /*
     * =================================================
     * 스크랩 관련
     * =================================================
     */
    public ScrapListResponse getPostingScrapList(String accessToken, int pageNumber) {
        Page<PostingScrap> postingScrapPage = scrapService.findAllByUserAccount(
                userRetrievalService.getUserFromAccessToken(accessToken),
                PageRequest.of(pageNumber, Utils.PAGE_SIZE)
        );

        List<ScrapDetails> scrapDetailsList = new ArrayList<>();
        for (PostingScrap scrap : postingScrapPage.getContent().stream().toList()) {
            scrapDetailsList.add(ScrapDetails.toDTO(scrap));
        }

        return ScrapListResponse.toDTO(postingScrapPage, scrapDetailsList);
    }

    public ResponseCode addPostingScrap(String accessToken, Long postingId) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        Posting posting = postingRetrievalService.getPostingEntityById(postingId);

        if (!scrapService.existsByUserAccountAndContents(user, posting)) {
            scrapService.save(
                    PostingScrap.builder()
                            .userAccount(user)
                            .posting(posting)
                            .build()
            );
            log.info("스크랩 등록 완료");

            contentsRankingScoreUpdateService.addRankingScore(posting.getContents(), SCRAP_RANKING_SCORE);
            todayPopularScoreService.addPopularPostingScore(posting, TodayPopularScoreService.POPULAR_POSTING_SCRAP_SCORE);
            log.info("[게시글 스크랩 추가] 랭킹 점수 반영 완료");

            return ResponseCode.SCRAP_ADD_SUCCESS;
        } else {
            // 해당 스크랩이 이미 있을 때
            log.error("스크랩 등록 실패 : 이미 존재하는 스크랩");
            return ResponseCode.SCRAP_ALREADY_EXIST;
        }
    }

    public void deletePostingScrap(String accessToken, Long scrapId) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // 삭제할 데이터가 존재하지 않는 경우
        if (!scrapService.existsById(scrapId)) {
            log.error("스크랩 삭제 실패 : 유효하지 않은 ID");
            throw new BadRequestException(ResponseCode.SCRAP_WRONG_ID);
        }
        PostingScrap postingScrap = scrapService.findByIdAndUserAccount(scrapId, user);
        contentsRankingScoreUpdateService.subtractRankingScore(postingScrap.getPosting().getContents(), SCRAP_RANKING_SCORE);
        todayPopularScoreService.subtractPopularPostingScore(postingScrap.getPosting(), TodayPopularScoreService.POPULAR_POSTING_SCRAP_SCORE);
        log.info("[게시글 스크랩 취소] 랭킹 점수 반영 완료");

        scrapService.deleteByIdAndUserAccount(scrapId, user);
        log.info("스크랩 삭제 완료");
    }

    /*
     * =================================================
     * 작성/좋아요한 게시글
     * =================================================
     */
    public PostingListResponse getPostingList(String accessToken, int pageNumber) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        return postingService.getPostingListByUser(user, pageNumber);
    }

    public PostingListResponse getLikedPostingList(String accessToken, int pageNumber, int likeStatus) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        return postingService.getLikedPostingListByUser(user, pageNumber, likeStatus);
    }

    /*
     * =================================================
     * 작성/좋아요한 리뷰
     * =================================================
     */
    public ReviewListResponse getReviewList(String accessToken, int pageNumber) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        return reviewService.getReviewListByUser(user, pageNumber);
    }

    public ReviewListResponse getLikedReviewList(String accessToken, int pageNumber, int likeStatus) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        return reviewService.getLikedReviewListByUser(user, pageNumber, likeStatus);
    }
}
