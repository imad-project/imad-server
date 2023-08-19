package com.ncookie.imad.domain.networks.entity;

import com.ncookie.imad.domain.networks.dto.DetailsNetworks;
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
    private Long networksId;

    @Setter private String networksName;
    @Setter private String logoPath;
    @Setter private String originCountry;

    public static Networks toEntity(DetailsNetworks networks) {
        return Networks.builder()
                .networksId(networks.getId())
                .networksName(networks.getName())
                .logoPath(networks.getLogoPath())
                .originCountry(networks.getOriginCountry())
                .build();
    }
}
