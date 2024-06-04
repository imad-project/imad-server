package com.ncookie.imad.domain.useractivity.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.profile.entity.ContentsBookmark;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.useractivity.entity.ActivityType;
import com.ncookie.imad.domain.useractivity.entity.UserActivity;
import com.ncookie.imad.domain.useractivity.repository.UserActivityRepository;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
@Description("유저 활동을 기록")
public class UserActivityService {
    private final UserActivityRepository userActivityRepository;

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
        Optional<UserActivity> optionalUserActivity = userActivityRepository.findByContentsBookmark(contentsBookmark);

        optionalUserActivity.ifPresent(userActivityRepository::delete);
        log.info("활동 기록: 북마크 삭제");
    }
}
