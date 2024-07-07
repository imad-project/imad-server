package com.ncookie.imad.domain.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserModifyProfileImageResponse {
    private String url;

    public UserModifyProfileImageResponse(String url) {
        this.url = url;
    }

    static public UserModifyProfileImageResponse of(String url) {
        return new UserModifyProfileImageResponse(url);
    }
}
