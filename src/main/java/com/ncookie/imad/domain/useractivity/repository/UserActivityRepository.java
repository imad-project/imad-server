package com.ncookie.imad.domain.useractivity.repository;

import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.review.entity.Review;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.useractivity.entity.ActivityType;
import com.ncookie.imad.domain.useractivity.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    @Query("SELECT u FROM UserActivity u WHERE u.contents.contentsType = 'TV'")
    List<UserActivity> findAllByUserAccountWithTv(UserAccount userAccount);

    @Query("SELECT u FROM UserActivity u WHERE u.contents.contentsType = 'MOVIE'")
    List<UserActivity> findAllByUserAccountWithMovie(UserAccount userAccount);

    @Query("SELECT u FROM UserActivity u WHERE u.contents.contentsType = 'ANIMATION'")
    List<UserActivity> findAllByUserAccountWithAnimation(UserAccount userAccount);

    List<UserActivity> findAllByUserAccount(UserAccount userAccount);

    Optional<UserActivity> findByContentsBookmark(ContentsBookmark contentsBookmark);

    Optional<UserActivity> findByUserAccountAndReviewAndActivityType(UserAccount userAccount, Review review, ActivityType activityType);
    Optional<UserActivity> findByUserAccountAndPostingAndActivityType(UserAccount userAccount, Posting posting, ActivityType activityType);
}
