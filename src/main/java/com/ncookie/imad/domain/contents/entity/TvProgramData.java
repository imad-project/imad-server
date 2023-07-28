package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("tv")
@Entity
public class TvProgramData extends Contents {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tvProgramDataId;

    @Setter
    private LocalDate firstAirDate;

    @Setter
    private LocalDate lastAirDate;

    @Setter
    private int numberOfEpisodes;

    @Setter
    private int numberOfSeasons;
}
