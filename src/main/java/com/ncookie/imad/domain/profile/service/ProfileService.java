package com.ncookie.imad.domain.profile.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.posting.service.PostingRetrievalService;
import com.ncookie.imad.domain.profile.dto.BookmarkDetails;
import com.ncookie.imad.domain.profile.dto.BookmarkListResponse;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.profile.entity.PostingScrap;
import com.ncookie.imad.domain.review.dto.response.ReviewListResponse;
import com.ncookie.imad.domain.review.service.ReviewService;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ProfileService {
    private final UserRetrievalService userRetrievalService;
    private final ContentsService contentsService;
    private final ReviewService reviewService;
    private final PostingRetrievalService postingRetrievalService;

    private final BookmarkService bookmarkService;
    private final ScrapService scrapService;

    public BookmarkListResponse getContentsBookmarkList(String accessToken, int pageNumber) {
        int REVIEW_LIST_PAGE_SIZE = 10;
        Page<ContentsBookmark> contentsBookmarkPage = bookmarkService.findAllByUserAccount(
                userRetrievalService.getUserFromAccessToken(accessToken),
                PageRequest.of(pageNumber, REVIEW_LIST_PAGE_SIZE)
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
            bookmarkService.save(
                    ContentsBookmark.builder()
                            .userAccount(user)
                            .contents(contents)
                            .build()
            );
            return ResponseCode.BOOKMARK_ADD_SUCCESS;
        } else {
            // 해당 북마크가 이미 있을 때
            return ResponseCode.BOOKMARK_ALREADY_EXIST;
        }
    }

    // 작품 북마크 삭제
    public void deleteContentsBookmark(String accessToken, Long bookmarkId) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // 삭제할 데이터가 존재하지 않는 경우
        if (!bookmarkService.existsById(bookmarkId)) {
            throw new BadRequestException(ResponseCode.BOOKMARK_WRONG_ID);
        }
        bookmarkService.deleteByIdAndUserAccount(bookmarkId, user);
    }

    public ReviewListResponse getReviewList(String accessToken, int pageNumber) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        return reviewService.getReviewListByUser(user, pageNumber);
    }

    public ReviewListResponse getLikedReviewList(String accessToken, int pageNumber) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        return reviewService.getLikedReviewListByUser(user, pageNumber);
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
            return ResponseCode.SCRAP_ADD_SUCCESS;
        } else {
            // 해당 스크랩이 이미 있을 때
            return ResponseCode.SCRAP_ALREADY_EXIST;
        }
    }
}
