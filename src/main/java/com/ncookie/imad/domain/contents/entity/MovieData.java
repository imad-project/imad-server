package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
    private Integer runtime;
}
