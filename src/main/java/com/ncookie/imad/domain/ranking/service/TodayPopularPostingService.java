package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.posting.dto.response.PostingDetailsResponse;
import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.ranking.entity.TodayPopularPosting;
import com.ncookie.imad.domain.ranking.repository.TodayPopularPostingsRepository;
import com.ncookie.imad.global.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TodayPopularPostingService {
    private final TodayPopularPostingsRepository todayPopularPostingsRepository;

    public final static int POPULAR_SCRAP_SCORE = 10;
    public final static int POPULAR_LIKE_SCORE = 5;
    public final static int POPULAR_COMMENT_SCORE = 2;
    public final static int POPULAR_VIEW_CNT_SCORE = 1;


    public PostingDetailsResponse getTodayPopularPosting() {
        List<TodayPopularPosting> popularPostingList = todayPopularPostingsRepository.findTopByPopularScore();
        
        // 인기 게시글 데이터가 존재하지 않으면 null 반환
        if (popularPostingList.isEmpty()) {
            log.info("오늘의 게시글 데이터가 존재하지 않습니다.");
            return null;
        }

        // 인기 점수가 가장 높은 게시글이 2개 이상일 때 랜덤으로 반환
        if (popularPostingList.size() > 1) {
            int randomNum = Utils.getRandomNum(popularPostingList.size());
            
            log.info("인기 점수가 가장 높은 게시글이 2개 이상이므로 이 중 랜덤으로 반환합니다");
            return PostingDetailsResponse.toDTO(popularPostingList.get(randomNum).getPosting(), null);
        }
        
        log.info("오늘의 게시글 데이터를 반환합니다");
        return PostingDetailsResponse.toDTO(popularPostingList.get(0).getPosting(), null);
    }

    public void addPopularScore(Posting posting, int score) {
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

    public void subtractPopularScore(Posting posting, int score) {
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
