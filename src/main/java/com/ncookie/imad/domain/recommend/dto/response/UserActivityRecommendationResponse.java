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
@Description("작품 추천 데이터 - 서비스 활동 기반 추천")
public class UserActivityRecommendationResponse {
    // 유저의 활동 기록이 없으면 추천 데이터가 null일 수 있다.
    // 이 때에는 관련 메세지를 유저에게 보여주도록 하자
    private TmdbDiscoverTv userActivityRecommendationTv;
    private TmdbDiscoverMovie userActivityRecommendationMovie;
    private TmdbDiscoverTv userActivityRecommendationTvAnimation;
    private TmdbDiscoverMovie userActivityRecommendationMovieAnimation;
}
