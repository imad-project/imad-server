package com.ncookie.imad.domain.tmdb.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true, allowSetters = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TmdbDiscoverMovie {
    // 페이지 정보
    private Long page;
    private Long totalPages;
    private Long totalResults;

    private List<TmdbDiscoverMovieDetails> results;

    // 서비스 활동 기반 추천에서만 사용하는 데이터
    // 추천 시 Similar API에 요청했던 TMDB ID를 저장함
    private Long tmdbId;
}
