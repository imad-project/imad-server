package com.ncookie.imad.domain.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class DetailsCredits {
    @JsonProperty("cast")
    private List<DetailsPerson> cast;

    @JsonProperty("crew")
    private List<DetailsPerson> crew;
}
