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
    @Column(name = "networks_id")
    private Long networks_id;

    @Setter private String networksName;
    @Setter private String logoPath;
    @Setter private String originCountry;
}
