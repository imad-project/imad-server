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
@Description("작품 추천 데이터 - 장르 기반")
public class GenreRecommendationResponse {
    // 선호 장르 기반 추천 데이터
    private TmdbDiscoverTv preferredGenreRecommendationTv;
    private TmdbDiscoverMovie preferredGenreRecommendationMovie;
}
