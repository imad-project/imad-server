package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.ranking.entity.TodayPopularPosting;
import com.ncookie.imad.domain.ranking.repository.TodayPopularPostingsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TodayPopularScoreService {
    private final TodayPopularPostingsRepository todayPopularPostingsRepository;


    public final static int POPULAR_POSTING_SCRAP_SCORE = 10;
    public final static int POPULAR_POSTING_LIKE_SCORE = 5;
    public final static int POPULAR_POSTING_COMMENT_SCORE = 2;
    public final static int POPULAR_POSTING_VIEW_CNT_SCORE = 1;


    public void addPopularPostingScore(Posting posting, int score) {
        if (posting == null) {
            log.warn("인기 점수 반영 실패 : ID에 해당하는 리뷰가 존재하지 않음");
            return;
        }

        Optional<TodayPopularPosting> popularPostingOptional = todayPopularPostingsRepository.findByPosting(posting);
        if (popularPostingOptional.isPresent()) {
            TodayPopularPosting todayPopularPosting = popularPostingOptional.get();
            Long oldScore = todayPopularPosting.getPopularScore();

            todayPopularPosting.setPopularScore(oldScore + score);
            todayPopularPostingsRepository.save(todayPopularPosting);
        } else {
            todayPopularPostingsRepository.save(
                    TodayPopularPosting.builder()
                            .posting(posting)
                            .popularScore((long) score)
                            .build()
            );
        }

        log.info("게시글 인기 점수 추가 완료");
    }

    public void subtractPopularPostingScore(Posting posting, int score) {
        if (posting == null) {
            log.warn("인기 점수 반영 실패 : ID에 해당하는 리뷰가 존재하지 않음");
            return;
        }

        Optional<TodayPopularPosting> popularPostingOptional = todayPopularPostingsRepository.findByPosting(posting);
        if (popularPostingOptional.isPresent()) {
            TodayPopularPosting todayPopularPosting = popularPostingOptional.get();
            Long oldScore = todayPopularPosting.getPopularScore();

            todayPopularPosting.setPopularScore(oldScore - score);
            todayPopularPostingsRepository.save(todayPopularPosting);
        } else {
            todayPopularPostingsRepository.save(
                    TodayPopularPosting.builder()
                            .posting(posting)
                            .popularScore((long) score)
                            .build()
            );
        }

        log.info("게시글 인기 점수 추가 완료");
    }

    public void clearDaily() {
        todayPopularPostingsRepository.deleteAllInBatch();
    }
}
