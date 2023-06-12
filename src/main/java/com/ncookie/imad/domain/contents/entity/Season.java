package com.ncookie.imad.domain.contents.entity;

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
    @Column(name = "networks_id")
    private Long season_id;

    @Setter private String name;
    @Setter private LocalDate airDate;
    @Setter private int episodeCount;
    @Setter private String overview;
    @Setter private String posterPath;
    @Setter private String seasonNumber;
}
