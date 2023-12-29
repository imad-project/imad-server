package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.service.ContentsRetrievalService;
import com.ncookie.imad.domain.ranking.entity.ContentsDailyScore;
import com.ncookie.imad.domain.ranking.repository.ContentsDailyScoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ContentsRankingScoreUpdateService {
    private final ContentsRetrievalService contentsRetrievalService;

    private final ContentsDailyScoreRepository contentsDailyScoreRepository;

    // 작품 북마크 작성
    public final static int BOOKMARK_SCORE = 20;
    // 작품 리뷰 작성
    public final static int REVIEW_SCORE = 10;
    // 게시글 작성 점수
    public final static int POSTING_SCORE = 5;
    // 게시글 스크랩 점수
    public final static int SCRAP_SCORE = 3;
    // 리뷰/게시글 좋아요 점수
    public final static int LIKE_SCORE = 3;
    // 게시글 댓글 점수
    public final static int COMMENT_SCORE = 2;


    public void addRankingScore(Contents contents, int score) {
        if (contents == null) {
            log.warn("랭킹 점수 반영 실패 : ID에 해당하는 작품이 존재하지 않음");
        }

        int oldScore = contentsDailyScoreRepository
                .findByContents(contents)
                .getRankingScore();

        contentsDailyScoreRepository.save(
            ContentsDailyScore.builder()
                    .contents(contents)
                    .rankingScore(oldScore + score)
                    .build()
        );
        log.info("랭킹 점수 추가 완료");
    }

    public void subtractRankingScore(Contents contents, int score) {
        if (contents == null) {
            log.warn("랭킹 점수 반영 실패 : ID에 해당하는 작품이 존재하지 않음");
        }

        int oldScore = contentsDailyScoreRepository
                .findByContents(contents)
                .getRankingScore();

        if ((oldScore - score) < 0) {
            log.warn("랭킹 점수는 0점보다 낮을 수 없음");
            return;
        }

        contentsDailyScoreRepository.save(
                ContentsDailyScore.builder()
                        .contents(contents)
                        .rankingScore(oldScore - score)
                        .build()
        );
        log.info("랭킹 점수 차감 완료");
    }
}
