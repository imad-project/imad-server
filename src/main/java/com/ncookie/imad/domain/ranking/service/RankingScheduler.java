package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsRetrievalService;
import com.ncookie.imad.domain.ranking.data.RankingPeriod;
import com.ncookie.imad.domain.ranking.dto.ContentsData;
import com.ncookie.imad.domain.ranking.dto.RankingScoreDTO;
import com.ncookie.imad.domain.ranking.entity.*;
import com.ncookie.imad.domain.ranking.repository.ContentsDailyScoreRankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ncookie.imad.domain.ranking.service.RankingUtils.*;


@Slf4j
@RequiredArgsConstructor
@Component
public class RankingScheduler {
    private final ContentsRetrievalService contentsRetrievalService;
    private final RankingRepositoryService rankingRepositoryService;

    private final ContentsDailyScoreRankingRepository contentsDailyScoreRankingRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final int PERIOD_ALLTIME = 0;
    private final int PERIOD_WEEK = 7;
    private final int PERIOD_MONTH = 30;
    private final int expirationDays = 40;

    private final String dailyRankingScoreString = "daily_score_";


    @Description("매일 자정마다 Redis에 작품 랭킹 점수 저장")
    @Scheduled(cron = "0 0 0 * * ?")    // 자정마다 실행
//    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void saveContentsDailyRankingScore() {
        String redisKey = dailyRankingScoreString + getLastDate(1);

        // String 형태의 key를 가지고, Contents 데이터를 value로 가짐
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        // MySQL DB에 있는 당일 랭킹 점수를 Redis에 저장함
        List<ContentsDailyRankingScore> dailyScoreList = contentsDailyScoreRankingRepository.findAll();
        Set<ZSetOperations.TypedTuple<Object>> rankingTuples = new HashSet<>();
        for (ContentsDailyRankingScore dailyScore : dailyScoreList) {
            Contents contents = dailyScore.getContents();

            // 일일 작품 랭킹 점수 Redis에 저장
            rankingTuples.add(new DefaultTypedTuple<>(ContentsData.from(dailyScore.getContents()), (double) dailyScore.getRankingScore()));
            log.info(String.format("[%s][%s] 일일 작품 랭킹 점수 저장 (Redis)", redisKey, contents.getTranslatedTitle()));
        }

        // 일일 랭킹 데이터의 만료기간 설정 (40일)
        zSetOperations.add(redisKey, rankingTuples);
        redisTemplate.expire(redisKey, expirationDays, TimeUnit.DAYS);

        // 작품 일일 랭킹 점수 DB 초기화
        contentsDailyScoreRankingRepository.deleteAllInBatch();
        log.info("일일 작품 랭킹 점수 DB 초기화 완료");
    }

    @Scheduled(cron = "0 2 0 * * *")    // 매일 자정 2분 후에 실행
    //    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void updateRankingData() {
        updateWeeklyRankingData();
        updateMonthlyRankingData();
        updateAllTimeRankingData();
    }

    @Description("주간 작품 랭킹 정산")
    public void updateWeeklyRankingData() {
        updateRankingScore(RankingPeriod.WEEKLY, PERIOD_WEEK);

        rankingRepositoryService.rankingWeeklyDeleteAll();
        List<RankingBaseEntity> rankingBaseEntities = saveRankingData(RankingPeriod.WEEKLY);

        List<RankingWeekly> rankingWeeklyList = rankingBaseEntities.stream()
                .map(data -> (RankingWeekly) data)
                .toList();
        rankingRepositoryService.rankingWeeklySaveAll(rankingWeeklyList);
    }

    @Description("월간 작품 랭킹 정산")
    public void updateMonthlyRankingData() {
        updateRankingScore(RankingPeriod.MONTHLY, PERIOD_MONTH);

        rankingRepositoryService.rankingMonthlyDeleteAll();
        List<RankingBaseEntity> rankingBaseEntities = saveRankingData(RankingPeriod.MONTHLY);

        List<RankingMonthly> rankingMonthlyList = rankingBaseEntities.stream()
                .map(data -> (RankingMonthly) data)
                .toList();
        rankingRepositoryService.rankingMonthlySaveAll(rankingMonthlyList);
    }

    @Description("전체 작품 랭킹 정산")
    public void updateAllTimeRankingData() {
        updateRankingScore(RankingPeriod.ALL_TIME, PERIOD_ALLTIME);

        rankingRepositoryService.rankingAllTimeDeleteAll();
        List<RankingBaseEntity> rankingBaseEntities = saveRankingData(RankingPeriod.ALL_TIME);

        List<RankingAllTime> rankingAllTimeList = rankingBaseEntities.stream()
                .map(data -> (RankingAllTime) data)
                .toList();
        rankingRepositoryService.rankingAllTimeSaveAll(rankingAllTimeList);
    }

    @Description("기간별(주간/월간/전체) 랭킹 점수 합산 및 Redis 저장")
    public void updateRankingScore(RankingPeriod rankingPeriod, int PERIOD) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        String periodString = rankingPeriod.getValue();
        List<String> recentDates = getRecentDates(PERIOD);

        String todayDate = getLastDate(0);
        String yesterdayDate = getLastDate(1);

        // 랭킹 점수 합산 데이터 생성
        String destKey = periodString + "_score_" + todayDate;

        if (PERIOD == PERIOD_ALLTIME) {
            // 총합 랭킹 점수 데이터를 합산 및 저장함
            zSetOperations.unionAndStore(
                    destKey,
                    dailyRankingScoreString + yesterdayDate,
                    destKey);
        } else {
            // 장르별 랭킹 점수 데이터를 합산 및 저장함
            zSetOperations.unionAndStore(
                    "",
                    recentDates.stream().map(s -> dailyRankingScoreString + s).collect(Collectors.toList()),
                    destKey,
                    Aggregate.SUM);
        }
        log.info(String.format("[%s] 랭킹 점수 합산 완료", periodString));

        // 랭킹 점수 데이터 만료기간 설정
        redisTemplate.expire(destKey, expirationDays, TimeUnit.DAYS);
    }

    @Description("기간별(주간/월간/전체) 랭킹 점수 데이터 가공 후 MySQL 테이블에 저장")
    private List<RankingBaseEntity> saveRankingData(RankingPeriod rankingPeriod) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        String periodString = rankingPeriod.getValue();

        String todayDate = getLastDate(0);
        String yesterdayDate = getLastDate(1);

        // 최종 랭킹 데이터
        List<RankingBaseEntity> rankingList = new ArrayList<>();

        // 작품 타입별로 랭킹 변화 추적 및 데이터 저장
        String scoreDataKey = periodString + "_score_";

        // 랭킹 점수 데이터를 랭킹 점수 기준으로 내림차순으로 정렬하여 추출
        Set<ZSetOperations.TypedTuple<Object>> todayRankSet =
                zSetOperations.reverseRangeWithScores(
                        scoreDataKey + todayDate,
                        0,
                        -1);

        assert todayRankSet != null;
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = todayRankSet.iterator();
        long todayRank = 0L;
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            ContentsData contentsData = (ContentsData) next.getValue();

            if (contentsData == null) {
                log.error("랭킹 데이터에서 작품을 찾을 수 없음");
                continue;
            }

            // 전날 랭킹 데이터와 비교
            Long lastRank = zSetOperations.reverseRank(scoreDataKey + yesterdayDate, contentsData);
            Long changedRank;
            if (lastRank == null) {
                log.info("신규 작품 랭킹 진입!");
                changedRank = null;
            } else {
                changedRank = lastRank - todayRank;
            }

            final Contents contents = contentsRetrievalService.getContentsById(contentsData.getContentsId());
            if (contents == null) {
                log.warn("ID에 해당하는 작품을 찾을 수 없음");
                continue;
            }
            
            RankingScoreDTO dto = RankingScoreDTO.builder()
                    .contents(contents)
                    .contentsType(contents.getContentsType())
                    .rank(todayRank + 1)
                    .rankChanged(changedRank)
                    .rankingScore(Objects.requireNonNull(next.getScore()).longValue())
                    .build();
            switch (rankingPeriod) {
                case WEEKLY -> rankingList.add(RankingWeekly.toEntity(dto));
                case MONTHLY -> rankingList.add(RankingMonthly.toEntity(dto));
                case ALL_TIME -> rankingList.add(RankingAllTime.toEntity(dto));
            }

            // 다음 랭킹 데이터 탐색
            todayRank++;
        }
        
        log.info(String.format("[%s] 랭킹 점수 정산 완료", periodString));
        return rankingList;
    }
}
