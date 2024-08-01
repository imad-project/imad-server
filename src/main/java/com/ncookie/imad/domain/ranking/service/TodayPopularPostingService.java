package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.posting.dto.response.PostingDetailsResponse;
import com.ncookie.imad.domain.posting.service.PostingService;
import com.ncookie.imad.domain.ranking.entity.TodayPopularPosting;
import com.ncookie.imad.domain.ranking.repository.TodayPopularPostingsRepository;
import com.ncookie.imad.global.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TodayPopularPostingService {
    private final PostingService postingService;

    private final TodayPopularPostingsRepository todayPopularPostingsRepository;

    public PostingDetailsResponse getTodayPopularPosting(String accessToken) {
        List<TodayPopularPosting> popularPostingList = todayPopularPostingsRepository.findTopByPopularScore();
        PostingDetailsResponse todayPopularPosting;
        
        if (popularPostingList.isEmpty()) {
            // 인기 게시글 데이터가 존재하지 않으면 null 반환
            log.info("오늘의 게시글 데이터가 존재하지 않아 좋아요를 가장 많이 받은 게시글을 조회합니다.");
            todayPopularPosting = postingService.getMostLikePosting(accessToken);
        } else if (popularPostingList.size() > 1) {
            // 인기 점수가 가장 높은 게시글이 2개 이상일 때 랜덤으로 반환
            int randomNum = Utils.getRandomNum(popularPostingList.size());
            
            log.info("인기 점수가 가장 높은 게시글이 2개 이상이므로 이 중 랜덤으로 반환합니다");
            todayPopularPosting = postingService.getPosting(accessToken, popularPostingList.get(randomNum).getPosting(), true);
        } else {
            todayPopularPosting = postingService.getPosting(accessToken, popularPostingList.get(0).getPosting(), true);
        }

        log.info("오늘의 게시글 데이터를 반환합니다");
        return todayPopularPosting;
    }
}
