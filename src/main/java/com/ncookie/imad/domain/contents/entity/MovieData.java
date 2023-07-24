package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("movie")
@Entity
public class MovieData extends Contents {
    @Setter private LocalDate releaseDate;
    @Setter private int status;
    @Setter private int runtime;
}
