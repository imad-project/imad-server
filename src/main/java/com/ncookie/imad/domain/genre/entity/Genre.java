package com.ncookie.imad.domain.genre.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Genre {
    @Id
    private Long genreId;

    private String name;
    private int genreType;      // 둘 다(0), TV(1), MOVIE(2)
}
