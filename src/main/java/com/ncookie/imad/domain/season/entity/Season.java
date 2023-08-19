package com.ncookie.imad.domain.season.entity;

import com.ncookie.imad.domain.season.dto.DetailsSeason;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Season {
    @Id
    private Long seasonId;

    @Setter private String seasonName;
    @Setter private LocalDate airDate;
    @Setter private int episodeCount;

    @Column(length = 5000)
    @Setter private String overview;

    @Setter private String posterPath;
    @Setter private int seasonNumber;

    public static Season toEntity(DetailsSeason season) {
        return Season.builder()
                .seasonId(season.getId())
                .seasonName(season.getName())
                .airDate(season.getAirDate())
                .episodeCount(season.getEpisodeCount())
                .overview(season.getOverview())
                .posterPath(season.getPosterPath())
                .seasonNumber(season.getSeasonNumber())
                .build();
    }
}
