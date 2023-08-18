package com.ncookie.imad.domain.networks.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailsNetworks {
    private long id;
    private String logoPath;
    private String name;
    private String originCountry;
}
