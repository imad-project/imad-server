package com.ncookie.imad.domain.recommend.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.service.ContentsRetrievalService;
import com.ncookie.imad.domain.recommend.data.RecommendCategory;
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
    private final ContentsRetrievalService contentsRetrievalService;

    private final UserActivityBasedRecommendationService userActivityBasedRecommendationService;

    private final TmdbApiClient apiClient;


    // 작품 추천 종합 버전 (메인 페이지용)
    public AllRecommendationResponse getAllRecommendations(String accessToken) {
        GenreRecommendationResponse preferredGenreBasedRecommendation = getPreferredGenreBasedRecommendation(accessToken, ContentsType.ALL, 1);
        UserActivityRecommendationResponse userActivityRecommendationResponse = getUserActivityRecommendation(accessToken, 1);
        ImadRecommendationResponse imadRecommendation = getImadRecommendation(ContentsType.ALL, 1, RecommendCategory.ALL);
        TrendRecommendationResponse trendRecommendation = getTrendRecommendation(ContentsType.ALL, 1);

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

    // 장르 기반 추천 (TMDB API - discover 사용)
    public GenreRecommendationResponse getPreferredGenreBasedRecommendation(String accessToken, ContentsType contentsType, int pageNumber) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // 게스트 요청일 경우 추천 데이터를 반환하지 않음
        if (user == null) {
            return GenreRecommendationResponse.builder().build();
        }

        TmdbDiscoverTv tmdbDiscoverTv = null;
        TmdbDiscoverMovie tmdbDiscoverMovie = null;

        switch (contentsType) {
            case TV -> tmdbDiscoverTv = apiClient.discoverTvWithPreferredGenre(
                    pageNumber,
                    userRetrievalService.getPreferredTvGenres(user));
            case MOVIE -> tmdbDiscoverMovie = apiClient.discoverMovieWithPreferredGenre(
                    pageNumber,
                    userRetrievalService.getPreferredMovieGenres(user));
            case ALL -> {
                tmdbDiscoverTv = apiClient.discoverTvWithPreferredGenre(
                        pageNumber,
                        userRetrievalService.getPreferredTvGenres(user));
                tmdbDiscoverMovie = apiClient.discoverMovieWithPreferredGenre(
                        pageNumber,
                        userRetrievalService.getPreferredMovieGenres(user));
            }
        }

        return GenreRecommendationResponse.builder()
                .preferredGenreRecommendationTv(tmdbDiscoverTv)
                .preferredGenreRecommendationMovie(tmdbDiscoverMovie)
                .build();
    }

    // 서비스 활동 기반 추천 (TMDB API - Similar 사용)
    // 일괄 요청 시 사용됨
    public UserActivityRecommendationResponse getUserActivityRecommendation(String accessToken, int pageNumber) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        if (user == null) {
            return UserActivityRecommendationResponse.builder().build();
        }

        // 유저 활동 기록에서 TV, Movie, Animation 별로 추출
        Contents tv = userActivityBasedRecommendationService.selectWeightedRandomContents(user, ContentsType.TV);
        Contents movie = userActivityBasedRecommendationService.selectWeightedRandomContents(user, ContentsType.MOVIE);
        Contents animation = userActivityBasedRecommendationService.selectWeightedRandomContents(user, ContentsType.ANIMATION);

        TmdbDiscoverTv recommendedTv = tv != null ? apiClient.fetchTmdbSimilarTv(tv.getTmdbId(), pageNumber) : null;
        TmdbDiscoverMovie recommendedMovie = movie != null ? apiClient.fetchTmdbSimilarMovie(movie.getTmdbId(), pageNumber) : null;

        if (tv != null) {
            recommendedTv.setContentsId(tv.getContentsId());
        }
        if (movie != null) {
            recommendedMovie.setContentsId(movie.getContentsId());
        }

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
            TmdbDiscoverTv tvAnimation = apiClient.fetchTmdbSimilarTv(animation.getTmdbId(), pageNumber);

            tvAnimation.setContentsId(animation.getContentsId());
            recommendationResponse.setUserActivityRecommendationTvAnimation(tvAnimation);
        } else if (animation.getTmdbType().equals(ContentsType.MOVIE)) {
            TmdbDiscoverMovie movieAnimation = apiClient.fetchTmdbSimilarMovie(animation.getTmdbId(), pageNumber);

            movieAnimation.setContentsId(animation.getContentsId());
            recommendationResponse.setUserActivityRecommendationMovieAnimation(movieAnimation);
        }

        return recommendationResponse;
    }

    // 서비스 활동 기반 추천 데이터 개별 요청 시 사용
    public UserActivityRecommendationResponse getUserActivityAdditionalRecommendation(int pageNumber, Long contentsId) {
        Contents contents = contentsRetrievalService.getContentsById(contentsId);

        UserActivityRecommendationResponse build = UserActivityRecommendationResponse.builder().build();

        TmdbDiscoverTv tv = null;
        TmdbDiscoverMovie movie = null;

        if (contents.getTmdbType().equals(ContentsType.TV)) {
            tv = apiClient.fetchTmdbSimilarTv(contents.getTmdbId(), pageNumber);
        } else if (contents.getTmdbType().equals(ContentsType.MOVIE)) {
            movie = apiClient.fetchTmdbSimilarMovie(contents.getTmdbId(), pageNumber);
        }

        // 작품 타입(TV, Movie, Animation)에 따라 DTO 클래스에 할당
        if (contents.getContentsType().equals(ContentsType.TV)) {
            build.setUserActivityRecommendationTv(tv);
        } else if (contents.getContentsType().equals(ContentsType.MOVIE)) {
            build.setUserActivityRecommendationMovie(movie);
        } else if (contents.getContentsType().equals(ContentsType.ANIMATION)) {
            if (contents.getTmdbType().equals(ContentsType.TV)) {
                build.setUserActivityRecommendationTvAnimation(tv);
            } else if (contents.getTmdbType().equals(ContentsType.MOVIE)) {
                build.setUserActivityRecommendationMovieAnimation(movie);
            }
        }

        return build;
    }

    // IMAD 자체 추천 (TMDB API - Popular, Top Rated 사용)
    public ImadRecommendationResponse getImadRecommendation(ContentsType contentsType,
                                                            int pageNumber,
                                                            RecommendCategory category) {
        TmdbDiscoverTv popularTv = null;
        TmdbDiscoverTv topRatedTv = null;

        TmdbDiscoverMovie popularMovie = null;
        TmdbDiscoverMovie topRatedMovie = null;

        switch (contentsType) {
            case TV -> {
                if (category.equals(RecommendCategory.POPULAR)) popularTv = apiClient.fetchTmdbPopularTv(pageNumber);
                else if (category.equals(RecommendCategory.TOP_RATED)) topRatedTv = apiClient.fetchTmdbTopRatedTv(pageNumber);
                else if (category.equals(RecommendCategory.ALL)) {
                    popularTv = apiClient.fetchTmdbPopularTv(pageNumber);
                    topRatedTv = apiClient.fetchTmdbTopRatedTv(pageNumber);
                }
            }
            case MOVIE -> {
                if (category.equals(RecommendCategory.POPULAR)) popularMovie = apiClient.fetchTmdbPopularMovie(pageNumber);
                else if (category.equals(RecommendCategory.TOP_RATED)) topRatedMovie = apiClient.fetchTmdbTopRatedMovie(pageNumber);
                else if (category.equals(RecommendCategory.ALL)) {
                    popularMovie = apiClient.fetchTmdbPopularMovie(pageNumber);
                    topRatedMovie = apiClient.fetchTmdbTopRatedMovie(pageNumber);
                }
            }
            case ALL -> {
                if (category.equals(RecommendCategory.POPULAR)) {
                    popularTv = apiClient.fetchTmdbPopularTv(pageNumber);
                    topRatedTv = apiClient.fetchTmdbTopRatedTv(pageNumber);
                }
                else if (category.equals(RecommendCategory.TOP_RATED)) {
                    popularMovie = apiClient.fetchTmdbPopularMovie(pageNumber);
                    topRatedMovie = apiClient.fetchTmdbTopRatedMovie(pageNumber);
                }
                else if (category.equals(RecommendCategory.ALL)) {
                    popularTv = apiClient.fetchTmdbPopularTv(pageNumber);
                    topRatedTv = apiClient.fetchTmdbTopRatedTv(pageNumber);

                    popularMovie = apiClient.fetchTmdbPopularMovie(pageNumber);
                    topRatedMovie = apiClient.fetchTmdbTopRatedMovie(pageNumber);
                }
            }
        }

        return ImadRecommendationResponse.builder()
                .popularRecommendationTv(popularTv)
                .popularRecommendationMovie(popularMovie)
                .topRatedRecommendationTv(topRatedTv)
                .topRatedRecommendationMovie(topRatedMovie)
                .build();
    }

    // 트렌드 추천 (TMDB API - Trend 사용)
    public TrendRecommendationResponse getTrendRecommendation(ContentsType contentsType, int pageNumber) {
        TmdbDiscoverTv tmdbDiscoverTv = null;
        TmdbDiscoverMovie tmdbDiscoverMovie = null;

        switch (contentsType) {
            case TV -> tmdbDiscoverTv = apiClient.fetchTmdbTrendingTv(pageNumber);
            case MOVIE -> tmdbDiscoverMovie = apiClient.fetchTmdbTrendingMovie(pageNumber);
            case ALL -> {
                tmdbDiscoverTv = apiClient.fetchTmdbTrendingTv(pageNumber);
                tmdbDiscoverMovie = apiClient.fetchTmdbTrendingMovie(pageNumber);
            }
        }

        return TrendRecommendationResponse.builder()
                .trendRecommendationTv(tmdbDiscoverTv)
                .trendRecommendationMovie(tmdbDiscoverMovie)
                .build();
    }
}
