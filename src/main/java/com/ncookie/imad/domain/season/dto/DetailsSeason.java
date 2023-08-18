package com.ncookie.imad.domain.season.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailsSeason {
    private long id;

    private String name;
    private LocalDate airDate;
    private int episodeCount;
    private String overview;
    private String posterPath;
    private int seasonNumber;

    @JsonCreator
    public DetailsSeason(@JsonProperty("air_date") String airDateString) {
        this.airDate = LocalDate.parse(airDateString);
    }
}
