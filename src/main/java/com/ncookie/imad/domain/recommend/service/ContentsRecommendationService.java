package com.ncookie.imad.domain.recommend.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.recommend.dto.response.*;
import com.ncookie.imad.domain.tmdb.dto.TmdbDiscoverMovie;
import com.ncookie.imad.domain.tmdb.dto.TmdbDiscoverTv;
import com.ncookie.imad.domain.tmdb.openfeign.TmdbApiClient;
import com.ncookie.imad.domain.user.entity.UserAccount;
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
    private final UserActivityBasedRecommendationService userActivityBasedRecommendationService;

    private final TmdbApiClient apiClient;


    // 1. 추천 종류별로 작품을 받아와 전달
    // 2. 사용자가 추가로 원하는 종류의 추천 작품 추가 전달

    public AllRecommendationResponse getAllRecommendations(String accessToken, int pageNumber) {
        GenreRecommendationResponse preferredGenreBasedRecommendation = getPreferredGenreBasedRecommendation(accessToken, pageNumber);
        UserActivityRecommendationResponse userActivityRecommendationResponse = getUserActivityRecommendation(accessToken, pageNumber);
        ImadRecommendationResponse imadRecommendation = getImadRecommendation(pageNumber);
        TrendRecommendationResponse trendRecommendation = getTrendRecommendation(pageNumber);

        return AllRecommendationResponse.builder()
                .preferredGenreRecommendationTv(preferredGenreBasedRecommendation.getPreferredGenreRecommendationTv())
                .preferredGenreRecommendationMovie(preferredGenreBasedRecommendation.getPreferredGenreRecommendationMovie())

                .userActivityRecommendationTv(userActivityRecommendationResponse.getUserActivityRecommendationTv())
                .userActivityRecommendationMovie(userActivityRecommendationResponse.getUserActivityRecommendationMovie())
                .userActivityRecommendationTvAnimation(userActivityRecommendationResponse.getUserActivityRecommendationTvAnimation())
                .userActivityRecommendationMovieAnimation(userActivityRecommendationResponse.getUserActivityRecommendationMovieAnimation())

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

    public UserActivityRecommendationResponse getUserActivityRecommendation(String accessToken, int pageNumber) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // 유저 활동 기록에서 TV, Movie, Animation 별로 추출
        Contents tv = userActivityBasedRecommendationService.selectWeightedRandomContents(user, ContentsType.TV);
        Contents movie = userActivityBasedRecommendationService.selectWeightedRandomContents(user, ContentsType.MOVIE);
        Contents animation = userActivityBasedRecommendationService.selectWeightedRandomContents(user, ContentsType.ANIMATION);

        TmdbDiscoverTv recommendedTv = tv != null ? apiClient.fetchTmdbSimilarTv(tv.getTmdbId(), pageNumber) : null;
        TmdbDiscoverMovie recommendedMovie = movie != null ? apiClient.fetchTmdbSimilarMovie(movie.getTmdbId(), pageNumber) : null;

        UserActivityRecommendationResponse recommendationResponse = UserActivityRecommendationResponse.builder()
                .userActivityRecommendationTv(recommendedTv)
                .userActivityRecommendationMovie(recommendedMovie)
                .build();

        // 유저 활동 기록 중 애니메이션 관련이 없다면 그대로 반환
        if (animation == null) {
            return recommendationResponse;
        }

        // 애니메이션은 TV와 Movie 모두 될 수 있으므로 따로 처리
        if (animation.getTmdbType().equals(ContentsType.TV)) {
            recommendationResponse.setUserActivityRecommendationTvAnimation(
                    apiClient.fetchTmdbSimilarTv(animation.getTmdbId(), pageNumber)
            );
        } else if (animation.getTmdbType().equals(ContentsType.MOVIE)) {
            recommendationResponse.setUserActivityRecommendationMovieAnimation(
                    apiClient.fetchTmdbSimilarMovie(animation.getTmdbId(), pageNumber)
            );
        }

        return recommendationResponse;
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
