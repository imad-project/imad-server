package com.ncookie.imad.domain.tmdb.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailsTv extends DetailsResponse {
    String name;
}
