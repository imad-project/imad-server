package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("movie")
@Entity
public class MovieData extends Contents {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieDataId;

    @Setter private LocalDate releaseDate;
    @Setter private int releaseStatus;
    @Setter private int runtime;
}
