package com.ncookie.imad.domain.recommend.service;

import com.ncookie.imad.domain.recommend.dto.response.AllRecommendationResponse;
import com.ncookie.imad.domain.recommend.dto.response.GenreRecommendationResponse;
import com.ncookie.imad.domain.recommend.dto.response.ImadRecommendationResponse;
import com.ncookie.imad.domain.recommend.dto.response.TrendRecommendationResponse;
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

    public AllRecommendationResponse getAllRecommendations(String accessToken, int pageNumber) {
        GenreRecommendationResponse preferredGenreBasedRecommendation = getPreferredGenreBasedRecommendation(accessToken, pageNumber);
        ImadRecommendationResponse imadRecommendation = getImadRecommendation(pageNumber);
        TrendRecommendationResponse trendRecommendation = getTrendRecommendation(pageNumber);

        return AllRecommendationResponse.builder()
                .preferredGenreRecommendationTv(preferredGenreBasedRecommendation.getPreferredGenreRecommendationTv())
                .preferredGenreRecommendationMovie(preferredGenreBasedRecommendation.getPreferredGenreRecommendationMovie())
                .popularRecommendationTv(imadRecommendation.getPopularRecommendationTv())
                .popularRecommendationMovie(imadRecommendation.getPopularRecommendationMovie())
                .topRatedRecommendationTv(imadRecommendation.getTopRatedRecommendationTv())
                .topRatedRecommendationMovie(imadRecommendation.getTopRatedRecommendationMovie())
                .trendRecommendationTv(trendRecommendation.getTrendRecommendationTv())
                .trendRecommendationMovie(trendRecommendation.getTrendRecommendationMovie())
                .build();
    }

    public GenreRecommendationResponse getPreferredGenreBasedRecommendation(String accessToken, int pageNumber) {
        TmdbDiscoverTv tmdbDiscoverTv = apiClient.discoverTvWithPreferredGenre(
                pageNumber,
                userRetrievalService.getPreferredTvGenres(accessToken));
        TmdbDiscoverMovie tmdbDiscoverMovie = apiClient.discoverMovieWithPreferredGenre(
                pageNumber,
                userRetrievalService.getPreferredMovieGenres(accessToken));

        return GenreRecommendationResponse.builder()
                .preferredGenreRecommendationTv(tmdbDiscoverTv)
                .preferredGenreRecommendationMovie(tmdbDiscoverMovie)
                .build();
    }

    public ImadRecommendationResponse getImadRecommendation(int pageNumber) {
        TmdbDiscoverTv popularTv = apiClient.fetchTmdbPopularTv(pageNumber);
        TmdbDiscoverTv topRatedTv = apiClient.fetchTmdbTopRatedTv(pageNumber);

        TmdbDiscoverMovie popularMovie = apiClient.fetchTmdbPopularMovie(pageNumber);
        TmdbDiscoverMovie topRatedMovie = apiClient.fetchTmdbTopRatedMovie(pageNumber);

        return ImadRecommendationResponse.builder()
                .popularRecommendationTv(popularTv)
                .popularRecommendationMovie(popularMovie)
                .topRatedRecommendationTv(topRatedTv)
                .topRatedRecommendationMovie(topRatedMovie)
                .build();
    }

    public TrendRecommendationResponse getTrendRecommendation(int pageNumber) {
        TmdbDiscoverTv tmdbDiscoverTv = apiClient.fetchTmdbTrendingTv(pageNumber);
        TmdbDiscoverMovie tmdbDiscoverMovie = apiClient.fetchTmdbTrendingMovie(pageNumber);

        return TrendRecommendationResponse.builder()
                .trendRecommendationTv(tmdbDiscoverTv)
                .trendRecommendationMovie(tmdbDiscoverMovie)
                .build();
    }
}
