package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("tv")
@Entity
public class TvProgramData extends Contents {
    @Setter
    private LocalDate firstAirDate;

    @Setter
    private LocalDate lastAirDate;

    @Setter
    private Integer numberOfEpisodes;

    @Setter
    private Integer numberOfSeasons;
}
