package com.ncookie.imad.domain.user.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.user.entity.Gender;
import lombok.Getter;

import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class UserUpdateRequest {
    Gender gender;
    int ageRange;
    int profileImage;
    String nickname;

    Set<Long> preferredTvGenres;
    Set<Long> preferredMovieGenres;
}
