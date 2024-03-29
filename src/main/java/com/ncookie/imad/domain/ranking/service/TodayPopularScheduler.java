package com.ncookie.imad.domain.ranking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class TodayPopularScheduler {
    private final TodayPopularScoreService todayPopularScoreService;


    @Description("매일 자정마다 인기 리뷰 데이터 리셋")
    @Scheduled(cron = "0 0 0 * * ?")    // 자정마다 실행
    public void clearPopularReviewDaily() {
        todayPopularScoreService.clearPopularReviewScore();
        log.info("오늘의 리뷰 데이터 리셋 완료");
    }

    @Description("매일 자정마다 인기 게시글 데이터 리셋")
    @Scheduled(cron = "0 0 0 * * ?")    // 자정마다 실행
    public void clearPopularPostingDaily() {
        todayPopularScoreService.clearPopularPostingScore();
        log.info("오늘의 게시글 데이터 리셋 완료");
    }
}
