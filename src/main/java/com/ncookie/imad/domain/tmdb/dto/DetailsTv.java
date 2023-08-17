package com.ncookie.imad.domain.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonTypeName("tv")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailsTv extends DetailsResponse_ {
    private String name;
    private String originalName;

    private String firstAirDate;
    private String lastAirDate;

    private int numberOfEpisodes;
    private int numberOfSeasons;
}
