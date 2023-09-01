package com.ncookie.imad.domain.season.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.season.entity.Season;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
// TV 작품의 시즌 정보를 가지고 있는 DTO 클래스
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
        if (airDateString == null) {
            this.airDate = null;
        } else {
            this.airDate = LocalDate.parse(airDateString);
        }
    }

    public static DetailsSeason toDTO(Season season) {
        return  DetailsSeason.builder()
                .id(season.getSeasonId())
                .name(season.getSeasonName())
                .airDate(season.getAirDate())
                .episodeCount(season.getEpisodeCount())
                .overview(season.getOverview())
                .posterPath(season.getPosterPath())
                .seasonNumber(season.getSeasonNumber())
                .build();
    }
}
