package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("movie")
@Entity
public class MovieData extends Contents {
    @Setter
    private LocalDate releaseDate;

    @Setter
    private boolean releaseStatus;

    @Setter
    private int runtime;
}
