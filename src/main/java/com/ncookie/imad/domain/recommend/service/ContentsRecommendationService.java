package com.ncookie.imad.domain.recommend.service;

import com.ncookie.imad.domain.tmdb.openfeign.TmdbApiClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
@Description("다른 작품 추천 관련 Service들로부터 작품들을 받아와 유저에게 반환하는 서비스")
public class ContentsRecommendationService {
    private final TmdbApiClient apiClient;

    // 1. 추천 종류별로 작품을 받아와 전달
    // 2. 사용자가 추가로 원하는 종류의 추천 작품 추가 전달

    public void getPreferredGenreBasedRecommendation() {
        apiClient.discoverTvWithPreferredGenre();
        apiClient.discoverMovieWithPreferredGenre();
    }
}
