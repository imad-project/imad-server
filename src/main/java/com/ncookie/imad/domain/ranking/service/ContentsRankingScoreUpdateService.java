package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.ranking.entity.ContentsDailyRankingScore;
import com.ncookie.imad.domain.ranking.repository.ContentsDailyScoreRankingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ContentsRankingScoreUpdateService {
    private final ContentsDailyScoreRankingRepository contentsDailyScoreRankingRepository;

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
            return;
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
}
