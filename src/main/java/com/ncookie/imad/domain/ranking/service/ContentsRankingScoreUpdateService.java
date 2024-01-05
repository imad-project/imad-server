package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.service.ContentsRetrievalService;
import com.ncookie.imad.domain.ranking.dto.ContentsData;
import com.ncookie.imad.domain.ranking.entity.ContentsDailyRankingScore;
import com.ncookie.imad.domain.ranking.repository.ContentsDailyScoreRankingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.ncookie.imad.domain.contents.entity.ContentsType.*;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ContentsRankingScoreUpdateService {
    private final ContentsRetrievalService contentsRetrievalService;

    private final ContentsDailyScoreRankingRepository contentsDailyScoreRankingRepository;

    private final int PERIOD_ALLTIME = 0;
    private final int PERIOD_WEEK = 7;
    private final int PERIOD_MONTH = 30;


    // 작품 북마크 작성
    public final static int BOOKMARK_RANKING_SCORE = 20;

    // 작품 리뷰 작성
    public final static int REVIEW_RANKING_SCORE = 10;

    // 게시글 작성 점수
    public final static int POSTING_RANKING_SCORE = 5;

    // 게시글 스크랩 점수
    public final static int SCRAP_RANKING_SCORE = 3;

    // 리뷰/게시글 좋아요 점수
    public final static int LIKE_RANKING_SCORE = 3;

    // 게시글 댓글 점수
    public final static int COMMENT_RANKING_SCORE = 2;


    public void addRankingScore(Contents contents, int score) {
        if (contents == null) {
            log.warn("랭킹 점수 반영 실패 : ID에 해당하는 작품이 존재하지 않음");
        }

        Optional<ContentsDailyRankingScore> optionalDailyScore = contentsDailyScoreRankingRepository.findByContents(contents);
        ContentsDailyRankingScore dailyScore;
        if (optionalDailyScore.isPresent()) {

            dailyScore = optionalDailyScore.get();
            int oldScore = dailyScore.getRankingScore();

            dailyScore.setRankingScore(oldScore + score);
            contentsDailyScoreRankingRepository.save(dailyScore);
        } else {

            contentsDailyScoreRankingRepository.save(
                    ContentsDailyRankingScore.builder()
                            .contents(contents)
                            .rankingScore(score)
                            .build()
            );
        }

        log.info("랭킹 점수 추가 완료");
    }

    public void subtractRankingScore(Contents contents, int score) {
        if (contents == null) {
            log.warn("랭킹 점수 반영 실패 : ID에 해당하는 작품이 존재하지 않음");
        }

        Optional<ContentsDailyRankingScore> optionalDailyScore = contentsDailyScoreRankingRepository.findByContents(contents);
        ContentsDailyRankingScore dailyScore;
        int oldScore;
        if (optionalDailyScore.isPresent()) {
            dailyScore = optionalDailyScore.get();
            oldScore = dailyScore.getRankingScore();
        } else {
            log.error("해당하는 작품의 랭킹 점수를 찾을 수 없습니다.");
            return;
        }

        if ((oldScore - score) <= 0) {
            log.warn("랭킹 점수는 0점보다 낮으므로 데이터 삭제");
            contentsDailyScoreRankingRepository.delete(dailyScore);
            return;
        }

        dailyScore.setRankingScore(oldScore - score);
        contentsDailyScoreRankingRepository.save(dailyScore);
        log.info("랭킹 점수 차감 완료");
    }

    private final RedisTemplate<String, Object> redisTemplate;

    Map<ContentsType, String> genreStringMap = Map.of(
                                ContentsType.ALL, "_ALL_",
                                ContentsType.MOVIE, "_MOVIE_",
                                ContentsType.TV, "_TV_",
                                ContentsType.ANIMATION, "_ANIMATION_");

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
            zSetOperations.add(genreStringMap.get(ALL) + defaultKey, ContentsData.toDTO(dailyScore.getContents()), dailyScore.getRankingScore());

            // 장르별로 별도의 데이터로 랭킹 점수 저장
            switch (contents.getContentsType()) {
                case MOVIE -> key = genreStringMap.get(MOVIE) + defaultKey;
                case TV -> key = genreStringMap.get(TV) + defaultKey;
                case ANIMATION -> key = genreStringMap.get(ANIMATION) + defaultKey;
            }
            zSetOperations.add(key, ContentsData.toDTO(dailyScore.getContents()), dailyScore.getRankingScore());
            log.info(String.format("[%s][%s] 일일 작품 랭킹 점수 저장 완료 (Redis)", key, contents.getTranslatedTitle()));

            // 총합 작품 랭킹 점수 DB 저장
//            zSetOperations.unionAndStore(
//                    "alltime_score" + genreStringMap.get(contents.getContentsType()) + defaultKey,
//                    key,
//                    "alltime_score" + genreStringMap.get(contents.getContentsType()) + defaultKey);
//            log.info(String.format("[%s][%s] ALL TIME 장르별 작품 랭킹 점수 저장 완료", key, contents.getTranslatedTitle()));


            // 일일 작품 랭킹 점수 DB 초기화
            // TODO: 테스트 후 삭제 함수 원상복귀
//            contentsDailyScoreRankingRepository.deleteAllInBatch();
            log.info("일일 작품 랭킹 점수 DB 초기화 완료");
        }

    }

    @Description("주간 작품 랭킹 정산")
    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
//    @Scheduled(cron = "0 5 0 * * *")    // 매일 자정 5분 후에 실행
    public void updateWeeklyScoreAndRanking() {
        updateScoreAndRanking("weekly", PERIOD_WEEK);
    }

    @Description("월간 작품 랭킹 정산")
//    @Scheduled(cron = "0 5 0 * * *")    // 매일 자정 5분 후에 실행
    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void updateMonthlyScoreAndRanking() {
        updateScoreAndRanking("monthly", PERIOD_MONTH);
    }

    @Description("전체 작품 랭킹 정산")
//    @Scheduled(cron = "0 5 0 * * *")    // 매일 자정 5분 후에 실행
    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void updateAllTimeScoreAndRanking() {
        updateScoreAndRanking("alltime", PERIOD_ALLTIME);
    }

    public void updateScoreAndRanking(String periodString, int PERIOD) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        List<String> recentDates = getRecentDates(PERIOD);

        String todayDate = getLastDate(0);
        String yesterdayDate = getLastDate(1);

        // 랭킹 점수 합산 데이터 생성
        for (String genreString : genreStringMap.values()) {
            if (PERIOD == PERIOD_ALLTIME) {
                // 총합 랭킹 점수 데이터를 합산 및 저장함
                zSetOperations.unionAndStore(
                        periodString + "_score" + genreString + todayDate,
                        genreString + yesterdayDate,
                        periodString + "_score" + genreString + todayDate);
                log.info("ALL TIME 장르별 작품 랭킹 점수 저장 완료");
            } else {
                // 장르별 랭킹 점수 데이터를 합산 및 저장함
                zSetOperations.unionAndStore(
                        "",
                        recentDates.stream().map(s -> genreString + s).collect(Collectors.toList()),
                        periodString + "_score" + genreString + todayDate,
                        Aggregate.SUM);
            }

            log.info(String.format("%s 랭킹 점수 합산 완료", genreString));
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


    @Description("n일 전까지의 날짜 포맷 스트링을 리스트로 반환. 주간/월간 랭킹 점수를 정산할 때 사용됨")
    private List<String> getRecentDates(int days) {
        List<String> recentDates = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 오늘부터 최근 7일 동안의 날짜 생성
        for (int i = 0; i < days; i++) {
            LocalDate currentDate = LocalDate.now().minusDays(i + 1);
            String formattedDate = currentDate.format(formatter);
            recentDates.add(formattedDate);
        }

        return recentDates;
    }

    @Description("n일 전까지의 날짜 포맷 스트링을 리스트로 반환. 주간/월간 랭킹 점수를 정산할 때 사용됨")
    private String getLastDate(int n) {
        // 자정이 지났으므로 전날 날짜를 가져옴
        LocalDate currentDate = LocalDate.now().minusDays(n);

        // `20231231` 과 같은 형식으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return currentDate.format(formatter);
    }
}
