package com.ncookie.imad.domain.user.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.user.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@Getter
public class UserUpdateRequest {
    private Gender gender;
    private int birthYear;
    private String profileImage;
    private String nickname;

    private Set<Long> preferredTvGenres;
    private Set<Long> preferredMovieGenres;
}
