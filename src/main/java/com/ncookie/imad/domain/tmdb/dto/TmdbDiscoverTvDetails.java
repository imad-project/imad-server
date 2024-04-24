package com.ncookie.imad.domain.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true, allowSetters = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TmdbDiscoverTvDetails {
    private Long id;                // TMDB ID
    private String name;            // 제목
    private String posterPath;      // 포스터 경로
}
