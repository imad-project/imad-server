package com.ncookie.imad.domain.tmdb.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true, allowSetters = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TmdbDiscoverTv {
        // 페이지 정보
    private Long page;
    private Long totalPages;
    private Long totalResults;

    private List<TmdbDiscoverTvDetails> results;
}
