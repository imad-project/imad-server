package com.ncookie.imad.domain.season.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seasonId;

    @Setter private String seasonName;
    @Setter private LocalDate airDate;
    @Setter private int episodeCount;
    @Setter private String overview;
    @Setter private String posterPath;
    @Setter private int seasonNumber;
}