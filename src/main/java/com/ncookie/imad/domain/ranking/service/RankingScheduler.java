package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.ranking.data.RankingPeriod;
import com.ncookie.imad.domain.ranking.dto.ContentsData;
import com.ncookie.imad.domain.ranking.entity.ContentsDailyRankingScore;
import com.ncookie.imad.domain.ranking.repository.ContentsDailyScoreRankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ncookie.imad.domain.contents.entity.ContentsType.*;
import static com.ncookie.imad.domain.contents.entity.ContentsType.ANIMATION;
import static com.ncookie.imad.domain.ranking.service.RankingUtils.*;
import static com.ncookie.imad.domain.ranking.service.RankingUtils.genreStringMap;


@Slf4j
@RequiredArgsConstructor
@Component
public class RankingScheduler {
    private final ContentsDailyScoreRankingRepository contentsDailyScoreRankingRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final int PERIOD_ALLTIME = 0;
    private final int PERIOD_WEEK = 7;
    private final int PERIOD_MONTH = 30;
    private final int expirationDays = 40;


    @Description("매일 자정마다 Redis에 작품 랭킹 점수 저장")
//    @Scheduled(cron = "0 0 0 * * ?")    // 자정마다 실행
    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void saveContentsDailyRankingScore() {
        String defaultKey = getLastDate(1);

        // String 형태의 key를 가지고, Contents 데이터를 value로 가짐
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        // MySQL DB에 있는 당일 랭킹 점수를 Redis에 저장함
        List<ContentsDailyRankingScore> dailyScoreList = contentsDailyScoreRankingRepository.findAll();
        for (ContentsDailyRankingScore dailyScore : dailyScoreList) {
            Contents contents = dailyScore.getContents();
            String key = defaultKey;

            // 일일 작품 랭킹 점수 Redis에 저장
            zSetOperations.add(genreStringMap.get(ALL) + defaultKey, ContentsData.from(dailyScore.getContents()), dailyScore.getRankingScore());

            // 장르별로 별도의 데이터로 랭킹 점수 저장
            switch (contents.getContentsType()) {
                case MOVIE -> key = genreStringMap.get(MOVIE) + defaultKey;
                case TV -> key = genreStringMap.get(TV) + defaultKey;
                case ANIMATION -> key = genreStringMap.get(ANIMATION) + defaultKey;
            }
            zSetOperations.add(key, ContentsData.from(dailyScore.getContents()), dailyScore.getRankingScore());
            log.info(String.format("[%s][%s] 일일 작품 랭킹 점수 저장 완료 (Redis)", key, contents.getTranslatedTitle()));

            // 일일 작품 랭킹 점수 DB 초기화
//            contentsDailyScoreRankingRepository.deleteAllInBatch();
            log.info("일일 작품 랭킹 점수 DB 초기화 완료");
        }

        // 일일 랭킹 데이터의 만료기간 설정 (40일)
        for (String genreString : genreStringMap.values()) {
            redisTemplate.expire(genreString + defaultKey, expirationDays, TimeUnit.DAYS);
        }
    }

    @Description("주간 작품 랭킹 정산")
    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
//    @Scheduled(cron = "0 5 0 * * *")    // 매일 자정 5분 후에 실행
    public void updateWeeklyScoreAndRanking() {
        updateScoreAndRanking(RankingPeriod.WEEKLY, PERIOD_WEEK);
    }

    @Description("월간 작품 랭킹 정산")
//    @Scheduled(cron = "0 5 0 * * *")    // 매일 자정 5분 후에 실행
    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void updateMonthlyScoreAndRanking() {
        updateScoreAndRanking(RankingPeriod.MONTHLY, PERIOD_MONTH);
    }

    @Description("전체 작품 랭킹 정산")
    @Scheduled(cron = "0 5 0 * * *")    // 매일 자정 5분 후에 실행
//    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void updateAllTimeScoreAndRanking() {
        updateScoreAndRanking(RankingPeriod.MONTHLY, PERIOD_ALLTIME);
    }

    @Description("주간/월간/전체 작품 랭킹 최종 데이터의 정산 및 저장하는 메소드")
    public void updateScoreAndRanking(RankingPeriod rankingPeriod, int PERIOD) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        String periodString = rankingPeriod.getValue();
        List<String> recentDates = getRecentDates(PERIOD);

        String todayDate = getLastDate(0);
        String yesterdayDate = getLastDate(1);

        // 랭킹 점수 합산 데이터 생성
        for (String genreString : genreStringMap.values()) {
            String destKey = periodString + "_score" + genreString + todayDate;

            if (PERIOD == PERIOD_ALLTIME) {
                // 총합 랭킹 점수 데이터를 합산 및 저장함
                zSetOperations.unionAndStore(
                        destKey,
                        genreString + yesterdayDate,
                        destKey);
                log.info("ALL TIME 장르별 작품 랭킹 점수 저장 완료");
            } else {
                // 장르별 랭킹 점수 데이터를 합산 및 저장함
                zSetOperations.unionAndStore(
                        "",
                        recentDates.stream().map(s -> genreString + s).collect(Collectors.toList()),
                        destKey,
                        Aggregate.SUM);
                log.info(String.format("%s 랭킹 점수 합산 완료", genreString));
            }

            // 랭킹 점수 데이터 만료기간 설정
            redisTemplate.expire(destKey, expirationDays, TimeUnit.DAYS);
        }

        // 랭킹 변화 추적
        for (String genreString : genreStringMap.values()) {
            String weeklyScoreKey = periodString + "_score" + genreString;

            // 위에서 저장했던 랭킹 점수 데이터를 점수 기준 내림차순으로 정렬하여 추출
            Set<ZSetOperations.TypedTuple<Object>> todayRankSet =
                    zSetOperations.reverseRangeWithScores(
                            weeklyScoreKey + todayDate,
                            0,
                            -1);

            if (todayRankSet == null) {
                log.error("에러 발생! 주간 랭킹 점수 합산 데이터를 불러올 수 없음");
                return;
            }

            Iterator<ZSetOperations.TypedTuple<Object>> iterator = todayRankSet.iterator();
            Long todayRank = 0L;
            while (iterator.hasNext()) {
                ZSetOperations.TypedTuple<Object> next = iterator.next();
                ContentsData contentsData = (ContentsData) next.getValue();

                if (contentsData == null) {
                    log.info("랭킹에서 작품을 찾을 수 없음");
                    continue;
                }

                // 전날 랭킹 데이터와 비교
                Long lastRank = zSetOperations.reverseRank(weeklyScoreKey + yesterdayDate, contentsData);
                Long changedRank;
                if (lastRank == null) {
                    log.info("신규 작품 랭킹 진입!");
                    changedRank = null;
                } else {
                    changedRank = lastRank - todayRank;
                }

                // 랭킹 데이터 갱신
                contentsData.setRank(todayRank + 1);
                contentsData.setRankChanged(changedRank);
                zSetOperations.add(
                        periodString + "_ranking" + genreString + todayDate,
                        contentsData,
                        todayRank + 1);
                log.info(String.format("[%s][%s] 랭킹 데이터 갱신", periodString, genreString));

                // 다음 랭킹 데이터 탐색
                todayRank++;

                if (todayRank >= 100) {
                    log.info(String.format("[%s][%s] TOP 100 랭킹 정산 완료!", periodString, genreString));
                    break;
                }
            }
        }
    }
}
