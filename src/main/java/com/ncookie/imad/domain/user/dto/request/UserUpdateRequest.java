package com.ncookie.imad.domain.user.dto.request;

import com.ncookie.imad.domain.user.entity.Gender;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
    Gender gender;
    int ageRange;
    String profileImage;
}
