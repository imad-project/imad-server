package com.ncookie.imad.domain.contents.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Networks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long networksId;

    @Setter private String networksName;
    @Setter private String logoPath;
    @Setter private String originCountry;
}
