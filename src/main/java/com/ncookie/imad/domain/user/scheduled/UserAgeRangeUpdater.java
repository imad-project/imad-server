package com.ncookie.imad.domain.user.scheduled;

import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserAgeRangeUpdater {
    private final UserAccountRepository userAccountRepository;

    @Description("연도가 바뀔 때마다 유저의 연령대 갱신")
    @Scheduled(cron = "0 0 0 1 1 ?") // 매년 1월 1일 자정에 실행
    public void updateUserAgeRange() {
        List<UserAccount> allUserList = userAccountRepository.findAll();
        int nowYear = LocalDate.now().getYear();

        log.info("유저 연령대 데이터 갱신 시작");
        for (UserAccount user : allUserList) {
            int ageRange = (nowYear - user.getBirthYear()) / 10;
            user.setAgeRange(ageRange);
        }
        log.info("유저 연령대 데이터 갱신 완료");

        log.info("유저 연령대 데이터 저장 중...");
        userAccountRepository.saveAll(allUserList);
        log.info("유저 연령대 데이터 저장 완료");
    }
}
