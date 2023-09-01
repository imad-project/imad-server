package com.ncookie.imad.domain.networks.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.networks.entity.Networks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
// TV 작품을 방영한 방송사의 정보를 가지고 있는 DTO 클래스
public class DetailsNetworks {
    private long id;
    private String logoPath;
    private String name;
    private String originCountry;

    public static DetailsNetworks toDTO(Networks networks) {
        return DetailsNetworks.builder()
                .id(networks.getNetworksId())
                .logoPath(networks.getLogoPath())
                .name(networks.getNetworksName())
                .originCountry(networks.getOriginCountry())
                .build();
    }
}
