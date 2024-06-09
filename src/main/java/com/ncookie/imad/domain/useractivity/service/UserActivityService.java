package com.ncookie.imad.domain.useractivity.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.useractivity.entity.ActivityType;
import com.ncookie.imad.domain.useractivity.entity.UserActivity;
import com.ncookie.imad.domain.useractivity.repository.UserActivityRepository;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
@Description("유저 활동을 기록")
public class UserActivityService {
    private final UserActivityRepository userActivityRepository;

    public List<UserActivity> getUserActivityListWithTv(UserAccount user) {
        return userActivityRepository.findAllByUserAccountWithTv(user);
    }

    public List<UserActivity> getUserActivityListWithMovie(UserAccount user) {
        return userActivityRepository.findAllByUserAccountWithMovie(user);
    }

    public List<UserActivity> getUserActivityListWithAnimation(UserAccount user) {
        return userActivityRepository.findAllByUserAccountWithAnimation(user);
    }

    public void addContentsBookmark(UserAccount user, Contents contents, ContentsBookmark bookmark) {
        userActivityRepository.save(
                UserActivity.builder()
                        .userAccount(user)
                        .contents(contents)
                        .contentsBookmark(bookmark)
                        .activityType(ActivityType.CONTENTS_BOOKMARK)
                        .build()
        );
        log.info("활동 기록: 북마크 추가");
    }
    
    public void removeContentsBookmark(ContentsBookmark contentsBookmark) {
        userActivityRepository.findByContentsBookmark(contentsBookmark)
                .ifPresent(userActivityRepository::delete);
        
        log.info("활동 기록: 북마크 삭제");
    }

    public void addWritingReview(UserAccount user, Contents contents, Review review) {
        userActivityRepository.save(
                UserActivity.builder()
                        .userAccount(user)
                        .contents(contents)
                        .review(review)
                        .activityType(ActivityType.WRITING_REVIEW)
                        .build()
        );
        log.info("활동 기록: 리뷰 작성");
    }

    public void removeWritingReview(UserAccount user, Review review) {
        userActivityRepository.findByUserAccountAndReviewAndActivityType(
                user, review, ActivityType.WRITING_REVIEW
        ).ifPresent(userActivityRepository::delete);

        log.info("활동 기록: 리뷰 삭제");
    }

    public void addReviewLike(UserAccount user, Contents contents, Review review) {
        userActivityRepository.save(
                UserActivity.builder()
                        .userAccount(user)
                        .contents(contents)
                        .review(review)
                        .activityType(ActivityType.LIKE_REVIEW)
                        .build()
        );
        log.info("활동 기록: 리뷰 좋아요");
    }

    public void removeReviewLike(UserAccount user, Review review) {
        userActivityRepository.findByUserAccountAndReviewAndActivityType(
                user, review, ActivityType.LIKE_REVIEW)
                .ifPresent(userActivityRepository::delete);

        log.info("활동 기록: 리뷰 좋아요 삭제");
    }
    
    public void addWritingPosting(UserAccount user, Contents contents, Posting posting) {
        userActivityRepository.save(
                UserActivity.builder()
                        .userAccount(user)
                        .contents(contents)
                        .posting(posting)
                        .activityType(ActivityType.WRITING_POSTING)
                        .build()
        );
        log.info("활동 기록: 게시글 작성");
    }

    public void removeWritingPosting(UserAccount user, Posting posting) {
        userActivityRepository.findByUserAccountAndPostingAndActivityType(
                user, posting, ActivityType.WRITING_POSTING
        ).ifPresent(userActivityRepository::delete);

        log.info("활동 기록: 게시글 삭제");
    }

    public void addPostingLike(UserAccount user, Contents contents, Posting posting) {
        userActivityRepository.save(
                UserActivity.builder()
                        .userAccount(user)
                        .contents(contents)
                        .posting(posting)
                        .activityType(ActivityType.LIKE_POSTING)
                        .build()
        );
        log.info("활동 기록: 게시글 좋아요");
    }

    public void removePostingLike(UserAccount user, Posting posting) {
        userActivityRepository.findByUserAccountAndPostingAndActivityType(
                user, posting, ActivityType.LIKE_POSTING)
                .ifPresent(userActivityRepository::delete);

        log.info("활동 기록: 게시글 좋아요 삭제");
    }
}
