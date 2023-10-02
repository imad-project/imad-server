package com.ncookie.imad.domain.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncookie.imad.domain.user.entity.AuthProvider;
import com.ncookie.imad.domain.user.entity.Gender;
import com.ncookie.imad.domain.user.entity.Role;
import com.ncookie.imad.domain.user.entity.UserAccount;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoResponse {
    private String email;
    private String nickname;
    
    // 로그인 주체. 서비스 자체 회원 또는 소설 회원 등이 있음
    private AuthProvider authProvider;

    private Gender gender;
    
    // 연령대
    private int ageRange;

    private int profileImage;

    // 유저의 추가정보 입력여부를 구분하기 위한 플래그 변수
    private Role role;

    private Set<Long> preferredTvGenres;
    private Set<Long> preferredMovieGenres;


    public static UserInfoResponse toDTO(UserAccount userAccount) {
        return UserInfoResponse.builder()
                .email(userAccount.getEmail())
                .nickname(userAccount.getNickname())
                .authProvider(userAccount.getAuthProvider())
                .gender(userAccount.getGender())
                .ageRange(userAccount.getAgeRange())
                .profileImage(userAccount.getProfileImage())

                .preferredTvGenres(userAccount.getPreferredTvGenres())
                .preferredMovieGenres(userAccount.getPreferredMovieGenres())

                .role(userAccount.getRole())
                .build();
    }
}
