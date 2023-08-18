package com.ncookie.imad.domain.tmdb.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.networks.dto.DetailsNetworks;
import com.ncookie.imad.domain.season.dto.DetailsSeason;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, allowSetters = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TmdbDetails {
    private long contentsId;
    private long tmdbId;

    private String overview;
    private String tagline;
    private String posterPath;
    private String originalLanguage;
    private String certification;

    private Set<Integer> genres;
    private Set<String> productionCountries;


    // IMAD Data
    private ContentsType contentsType;


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

    private List<DetailsSeason> seasons;
    private List<DetailsNetworks> networks;


    @JsonCreator
    public TmdbDetails(
            @JsonProperty("id") int id,
            @JsonProperty("genres") Set<DetailsGenre> genreSet,
            @JsonProperty("production_countries") Set<ProductionCountry> productionCountrySet) {
        this.tmdbId = id;

        this.genres = new HashSet<>();
        genreSet.forEach(genre -> this.genres.add(genre.getId()));

        this.productionCountries = new HashSet<>();
        productionCountrySet.forEach(country -> this.productionCountries.add(country.getIso_3166_1()));
    }
}
