package com.ncookie.imad.domain.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailsCredits {
    @JsonProperty("cast")
    private List<DetailsPerson> cast;

    @JsonProperty("crew")
    private List<DetailsPerson> crew;
}
