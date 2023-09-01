package com.ncookie.imad.domain.contents.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchResult {
    private int id;     // TMDB에서 사용하는 id

    // For Movie
    private String title;               // 번역된 제목
    private String original_title;      // 원본 제목
    private LocalDate releaseDate;      // 개봉일

    // For TV shows
    private String name;                // 번역된 제목
    private String original_name;       // 원본 제목
    private LocalDate firstAirDate;     // 첫 방영일

    private List<String> origin_country;    // 원본 국가
    private String original_language;       // 원본 언어

    private boolean adult;                  // 성인여부. 일반적인 19세 등급을 받은 작품이 아니라, 포르노와 같은 성인용 작품이 여기에 해당됨
    private String backdrop_path;           // 배경 포스터
    private String overview;                // 작품 상세설명
    private String poster_path;             // 포스터
    private String media_type;              // 쓰이는 것을 본 적이 없어서 잘 모르겠음
    private List<Integer> genreIds;         // 장르 리스트
    private boolean video;                  // 프리뷰 영상
}
