package com.ncookie.imad.domain.user.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.user.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
public class UserUpdateRequest {
    private Gender gender;
    private int birthYear;
    private int profileImage;
    private String nickname;

    private Set<Long> preferredTvGenres;
    private Set<Long> preferredMovieGenres;
}
