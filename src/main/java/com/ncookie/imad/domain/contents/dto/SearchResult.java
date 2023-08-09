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
    private int id;

    // For Movie
    private String title;
    private String original_title;
    private LocalDate releaseDate;

    // For TV shows
    private String name;
    private String original_name;
    private LocalDate firstAirDate;

    private List<String> origin_country;
    private String original_language;

    private boolean adult;
    private String backdrop_path;
    private String overview;
    private String poster_path;
    private String media_type;
    private List<Integer> genreIds;
    private boolean video;
}
