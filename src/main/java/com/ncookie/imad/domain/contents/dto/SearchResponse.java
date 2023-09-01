package com.ncookie.imad.domain.contents.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchResponse {
    private int page;           // 해당 검색결과의 페이지
    private int totalPages;     // 검색 결과 총 페이지 수
    private int totalResults;   // 검색 결과 데이터 총 개수

    @JsonProperty("results")
    private List<SearchResult> results;     // 검색결과 상세 데이터
}
