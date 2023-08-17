package com.ncookie.imad.domain.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonTypeName("movie")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DetailsMovie extends DetailsResponse_ {

    private String title;
    private String originalTitle;

    private String releaseDate;
    private int runtime;

    private String status;
}
