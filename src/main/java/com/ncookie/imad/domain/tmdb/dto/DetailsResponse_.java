package com.ncookie.imad.domain.tmdb.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, allowSetters = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tmdb_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DetailsMovie.class, name = "movie"),
        @JsonSubTypes.Type(value = DetailsTv.class, name = "tv")
})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailsResponse_ {
    private String tmdbType;

    private long id;
    private String overview;
    private String tagline;
    private String posterPath;
    private String originalLanguage;

    private Set<DetailsGenre> genres;
//    private Set<Integer> cgenres;
    private Set<ProductionCountry> productionCountries;

//    @JsonCreator
//    public DetailsResponse(@JsonProperty("genres") Set<DetailsGenre> genreSet) {
//        cgenres = new HashSet<>();
//        genreSet.forEach(genre -> this.cgenres.add(genre.getId()));
//    }
}
