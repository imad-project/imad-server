package com.ncookie.imad.domain.tmdb.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, allowSetters = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailsResponse {
    private String tmdbType;

    private long id;
    private String overview;
    private String tagline;
    private String posterPath;
    private String originalLanguage;

    private Set<Integer> genres;
    private Set<String> productionCountries;

    // Movie Data
    private String title;
    private String originalTitle;

    private String releaseDate;
    private int runtime;

    private String status;

    // TV Data
    private String name;
    private String originalName;

    private String firstAirDate;
    private String lastAirDate;

    private int numberOfEpisodes;
    private int numberOfSeasons;

    @JsonCreator
    public DetailsResponse(@JsonProperty("genres") Set<DetailsGenre> genreSet,
                           @JsonProperty("production_countries") Set<ProductionCountry> productionCountrySet) {
        this.genres = new HashSet<>();
        genreSet.forEach(genre -> this.genres.add(genre.getId()));

        this.productionCountries = new HashSet<>();
        productionCountrySet.forEach(country -> this.productionCountries.add(country.getIso_3166_1()));
    }
}
