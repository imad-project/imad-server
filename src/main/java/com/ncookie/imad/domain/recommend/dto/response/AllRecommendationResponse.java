package com.ncookie.imad.domain.recommend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.tmdb.dto.TmdbDiscoverMovie;
import com.ncookie.imad.domain.tmdb.dto.TmdbDiscoverTv;
import jdk.jfr.Description;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true, allowSetters = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Description("작품 추천 데이터 - 전체")
public class AllRecommendationResponse {
    // 선호 장르 기반 추천
    private TmdbDiscoverTv preferredGenreRecommendationTv;
    private TmdbDiscoverMovie preferredGenreRecommendationMovie;
    
    // 서비스 활동 기반 추천
    private TmdbDiscoverTv userActivityRecommendationTv;
    private TmdbDiscoverMovie userActivityRecommendationMovie;
    private TmdbDiscoverTv userActivityRecommendationTvAnimation;
    private TmdbDiscoverMovie userActivityRecommendationMovieAnimation;

    // 인기 작품 추천
    private TmdbDiscoverTv popularRecommendationTv;
    private TmdbDiscoverMovie popularRecommendationMovie;

    // 상위 평점 작품 추천
    private TmdbDiscoverTv topRatedRecommendationTv;
    private TmdbDiscoverMovie topRatedRecommendationMovie;

    // 트렌드 작품 추천
    private TmdbDiscoverTv trendRecommendationTv;
    private TmdbDiscoverMovie trendRecommendationMovie;
}

