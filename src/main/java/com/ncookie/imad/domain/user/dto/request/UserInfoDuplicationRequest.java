package com.ncookie.imad.domain.user.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
// 이메일 및 닉네임 중복검사 요청에서 사용되는 DTO 클래스
public class UserInfoDuplicationRequest {
    private String info;    // 이메일 또는 닉네임이 중복되는지 확인하기 위해 사용되는 변수
}
