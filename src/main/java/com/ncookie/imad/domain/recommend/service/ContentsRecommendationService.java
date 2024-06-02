package com.ncookie.imad.domain.recommend.service;

import com.ncookie.imad.domain.recommend.dto.response.ContentsRecommendationResponse;
import com.ncookie.imad.domain.tmdb.dto.TmdbDiscoverMovie;
import com.ncookie.imad.domain.tmdb.dto.TmdbDiscoverTv;
import com.ncookie.imad.domain.tmdb.openfeign.TmdbApiClient;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
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
    private final UserRetrievalService userRetrievalService;

    private final TmdbApiClient apiClient;


    // 1. 추천 종류별로 작품을 받아와 전달
    // 2. 사용자가 추가로 원하는 종류의 추천 작품 추가 전달

    public ContentsRecommendationResponse getPreferredGenreBasedRecommendation(String accessToken, int pageNumber) {
        TmdbDiscoverTv tmdbDiscoverTv = apiClient.discoverTvWithPreferredGenre(
                pageNumber,
                userRetrievalService.getPreferredTvGenres(accessToken));
        TmdbDiscoverMovie tmdbDiscoverMovie = apiClient.discoverMovieWithPreferredGenre(
                pageNumber,
                userRetrievalService.getPreferredMovieGenres(accessToken));

        return ContentsRecommendationResponse.builder()
                .preferredGenreRecommendationTv(tmdbDiscoverTv)
                .preferredGenreRecommendationMovie(tmdbDiscoverMovie)
                .build();
    }

    public void getImadRecommendation(String accessToken, int pageNumber) {
        TmdbDiscoverTv tmdbDiscoverTv = apiClient.fetchTmdbPopularTv(pageNumber);
        TmdbDiscoverTv tmdbDiscoverTv1 = apiClient.fetchTmdbTopRatedTv(pageNumber);
        TmdbDiscoverTv tmdbDiscoverTv2 = apiClient.fetchTmdbTrendingTv();

        TmdbDiscoverMovie tmdbDiscoverMovie = apiClient.fetchTmdbPopularMovie(pageNumber);
        TmdbDiscoverMovie tmdbDiscoverMovie1 = apiClient.fetchTmdbTopRatedMovie(pageNumber);
        TmdbDiscoverMovie tmdbDiscoverMovie2 = apiClient.fetchTmdbTrendingMovie();

        return;
    }
}
