package com.ncookie.imad.domain.useractivity.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.useractivity.entity.ActivityType;
import com.ncookie.imad.domain.useractivity.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    Optional<UserActivity> findByContentsBookmark(ContentsBookmark contentsBookmark);
}
