package com.ncookie.imad.dto;

import com.ncookie.imad.domain.UserAccount;
import com.ncookie.imad.domain.type.AuthProvider;
import com.ncookie.imad.domain.type.Gender;
import com.ncookie.imad.domain.type.Role;


public record UserAccountDto(
        String userId,
        String nickname,
        String userPassword,
        Gender gender,
        String email,
        int ageRange,
        String profileImage,
        Role role,
        AuthProvider authProvider
) {
    public static UserAccountDto of(
            String userId,
            String nickname,
            String userPassword,
            Gender gender,
            String email,
            int ageRange,
            String profileImage,
            Role role,
            AuthProvider authProvider
    ) {
        return new UserAccountDto(userId, nickname, userPassword, gender, email, ageRange, profileImage, role, authProvider);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getUserId(),
                entity.getNickname(),
                entity.getUserPassword(),
                entity.getGender(),
                entity.getEmail(),
                entity.getAgeRange(),
                entity.getProfileImageUrl(),
                entity.getRole(),
                entity.getAuthProvider()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(
                userId,
                nickname,
                userPassword,
                gender,
                email,
                ageRange,
                profileImage,
                role,
                authProvider
        );
    }
}
