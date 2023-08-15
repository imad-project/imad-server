package com.ncookie.imad.domain.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailsResponse {
    protected long id;
    protected String overview;
    protected String tagline;
    protected String posterPath;
    protected String originalLanguage;

    protected Set<DetailsGenre> genres;
    protected Set<ProductionCountry> productionCountries;
}
