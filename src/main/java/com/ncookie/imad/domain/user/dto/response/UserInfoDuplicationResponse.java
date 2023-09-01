package com.ncookie.imad.domain.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoDuplicationResponse {
    private boolean validation;     // 중복 검사 결과 플래그. 중복이 아니라면 true, 중복 데이터라면 false 값을 가진다.
}
