package com.ncookie.imad.domain.search.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchContentsRequest {
    private ContentsType contentsType;
    private String query;

    private List<Integer> genreList;
    private List<String> countryList;

    private LocalDate releaseDateMin;
    private LocalDate releaseDateMax;

    private Float scoreMin;
    private Float scoreMax;
    private boolean isNullScoreOk;
}
