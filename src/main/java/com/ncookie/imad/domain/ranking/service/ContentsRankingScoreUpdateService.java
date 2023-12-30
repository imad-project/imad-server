package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsRetrievalService;
import com.ncookie.imad.domain.ranking.entity.ContentsDailyScore;
import com.ncookie.imad.domain.ranking.repository.ContentsDailyScoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
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

    private final ContentsDailyScoreRepository contentsDailyScoreRepository;


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

        Optional<ContentsDailyScore> optionalDailyScore = contentsDailyScoreRepository.findByContents(contents);
        ContentsDailyScore dailyScore;
        if (optionalDailyScore.isPresent()) {
            dailyScore = optionalDailyScore.get();
            int oldScore = dailyScore.getRankingScore();

            dailyScore.setRankingScore(oldScore + score);
            contentsDailyScoreRepository.save(dailyScore);
        } else {
            contentsDailyScoreRepository.save(
                    ContentsDailyScore.builder()
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

        Optional<ContentsDailyScore> optionalDailyScore = contentsDailyScoreRepository.findByContents(contents);
        ContentsDailyScore dailyScore;
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
            contentsDailyScoreRepository.delete(dailyScore);
            return;
        }

        dailyScore.setRankingScore(oldScore - score);
        contentsDailyScoreRepository.save(dailyScore);
        log.info("랭킹 점수 차감 완료");
    }

    private final RedisTemplate<String, Object> redisTemplate;
    @Description("매일 자정마다 Redis에 작품 랭킹 점수 저장")
    @Scheduled(cron = "0 0 0 * * ?")    // 자정마다 실행
//    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    public void saveContentsDailyRankingScore() {
        // 현재 날짜를 가져옴
        LocalDate currentDate = LocalDate.now();

        // `20231231` 과 같은 형식으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String key = currentDate.format(formatter);

        // 만약 같은 키의 데이터가 이미 존재한다면 삭제
        redisTemplate.delete(key);
        
        // String 형태의 key를 가지고, Contents 데이터를 value로 가짐
        ZSetOperations<String, Object> dailyScoreSet = redisTemplate.opsForZSet();

        // MySQL DB에 있는 당일 랭킹 점수를 Redis에 저장함
        List<ContentsDailyScore> dailyScoreList = contentsDailyScoreRepository.findAll();
        for (ContentsDailyScore dailyScore : dailyScoreList) {
            Hibernate.initialize(dailyScore.getContents());
            dailyScoreSet.add(key, dailyScore.getContents(), dailyScore.getRankingScore());
            log.info(String.format("[%s][%s] 작품 랭킹 점수 저장 완료", key, dailyScore.getContents().getTranslatedTitle()));
        }
    }
}
