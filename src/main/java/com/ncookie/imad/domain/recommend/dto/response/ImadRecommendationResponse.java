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
@Description("작품 추천 데이터 - 아이매드 자체 추천")
public class ImadRecommendationResponse {
    private TmdbDiscoverTv popularRecommendationTv;
    private TmdbDiscoverMovie popularRecommendationMovie;

    private TmdbDiscoverTv topRatedRecommendationTv;
    private TmdbDiscoverMovie topRatedRecommendationMovie;
}
