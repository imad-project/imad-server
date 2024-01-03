package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.service.ContentsRetrievalService;
import com.ncookie.imad.domain.ranking.dto.ContentsData;
import com.ncookie.imad.domain.ranking.entity.ContentsDailyRankingScore;
import com.ncookie.imad.domain.ranking.entity.ContentsAllTimeRankingScore;
import com.ncookie.imad.domain.ranking.repository.ContentsDailyScoreRankingRepository;
import com.ncookie.imad.domain.ranking.repository.ContentsAllTimeRankingScoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ContentsRankingScoreUpdateService {
    private final ContentsRetrievalService contentsRetrievalService;

    private final ContentsDailyScoreRankingRepository contentsDailyScoreRankingRepository;
    private final ContentsAllTimeRankingScoreRepository contentsAllTimeRankingScoreRepository;

    private final int DAYS_OF_WEEK = 7;
    private final int DAYS_OF_MONTH = 30;


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
    @Description("매일 자정마다 Redis에 작품 랭킹 점수 저장")
//    @Scheduled(cron = "0 0 0 * * ?")    // 자정마다 실행
    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void saveContentsDailyRankingScore() {
        // 자정이 지났으므로 전날 날짜를 가져옴
        LocalDate currentDate = LocalDate.now().minusDays(1);

        // `20231231` 과 같은 형식으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String defaultKey = currentDate.format(formatter);

        // String 형태의 key를 가지고, Contents 데이터를 value로 가짐
        ZSetOperations<String, Object> dailyScoreSet = redisTemplate.opsForZSet();

        // MySQL DB에 있는 당일 랭킹 점수를 Redis에 저장함
        List<ContentsDailyRankingScore> dailyScoreList = contentsDailyScoreRankingRepository.findAll();
        for (ContentsDailyRankingScore dailyScore : dailyScoreList) {
            Contents contents = dailyScore.getContents();
            String key = defaultKey;

            // 일일 작품 랭킹 점수 Redis에 저장
            dailyScoreSet.add(defaultKey, ContentsData.toDTO(dailyScore.getContents()), dailyScore.getRankingScore());

            // 장르별로 별도의 데이터로 랭킹 점수 저장
            switch (contents.getContentsType()) {
                case MOVIE -> key = defaultKey + "_" + ContentsType.MOVIE.getContentsType();
                case TV -> key = defaultKey + "_" + ContentsType.TV.getContentsType();
                case ANIMATION -> key = defaultKey + "_" + ContentsType.ANIMATION.getContentsType();
            }
            dailyScoreSet.add(key, ContentsData.toDTO(dailyScore.getContents()), dailyScore.getRankingScore());
            log.info(String.format("[%s][%s] 일일 작품 랭킹 점수 저장 완료 (Redis)", key, contents.getTranslatedTitle()));

            // 총합 작품 랭킹 점수 MySQL에 저장
            Optional<ContentsAllTimeRankingScore> optionalContentsEntireScore = contentsAllTimeRankingScoreRepository.findByContents(contents);
            if (optionalContentsEntireScore.isPresent()) {
                ContentsAllTimeRankingScore contentsAllTimeRankingScore = optionalContentsEntireScore.get();
                int oldScore = contentsAllTimeRankingScore.getRankingScore();
                contentsAllTimeRankingScore.setRankingScore(oldScore + dailyScore.getRankingScore());
                contentsAllTimeRankingScoreRepository.save(contentsAllTimeRankingScore);
            } else {
                contentsAllTimeRankingScoreRepository.save(
                    ContentsAllTimeRankingScore.builder()
                            .contents(contents)
                            .rankingScore(dailyScore.getRankingScore())
                            .build()
                );
            }
            log.info(String.format("[%s][%s] 전체 작품 랭킹 점수 저장 완료 (MySQL)", key, contents.getTranslatedTitle()));


            // 일일 작품 랭킹 점수 DB 초기화
            // TODO: 테스트 후 삭제 함수 원상복귀
//            contentsDailyScoreRankingRepository.deleteAllInBatch();
            log.info("일일 작품 랭킹 점수 DB 초기화 완료");
        }
    }

    @Description("주간 작품 랭킹 정산 및 MySQL에 저장")
    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
//    @Scheduled(cron = "0 5 0 * * *")    // 매일 자정 5분 후에 실행
    public void updateWeeklyScoreAndRanking() {
        List<String> recentDates = getRecentDates(DAYS_OF_WEEK);

        for (String key : recentDates) {
            // 작품 정보와 랭킹 점수를 내림차순(점수 높은 순)으로 정렬하여 받아온다.
            Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet()
                    .reverseRangeWithScores(key, 0, -1);
        }
    }

    @Description("월간 작품 랭킹 정산 및 MySQL에 저장")
    @Scheduled(cron = "0 5 0 * * *")    // 매일 자정 5분 후에 실행
    public void updateMonthlyScoreAndRanking() {
        List<String> recentDates = getRecentDates(DAYS_OF_MONTH);
    }

    @Description("매일 00시 10분에 주간/월간/전체 작품 랭킹을 Redis에 업데이트함")
    @Scheduled(cron = "0 10 0 * * *")    // 매일 자정 10분 후에 실행
    public void updateRankingRedisDB() {

    }

    @Description("n일 전까지의 날짜 포맷 스트링을 리스트로 반환. 주간/월간 랭킹 점수를 정산할 때 사용됨")
    private static List<String> getRecentDates(int days) {
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
}
