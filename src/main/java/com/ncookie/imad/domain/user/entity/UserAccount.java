package com.ncookie.imad.domain.user.entity;

import com.ncookie.imad.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "email", unique = true),
})
@Entity
public class UserAccount extends BaseTimeEntity {
    @Id
    private String userId;

    @Setter @Column(nullable = false, length = 50) private String nickname;
    @Setter @Column(length = 50) private String userPassword;

    @Setter private String email;               // 이메일
    @Setter private Gender gender;              // 성별, 0 : 남자, 1 : 여자
    @Setter private int ageRange;               // 연령대
    @Setter private String profileImageUrl;     // 프로필 이미지(URL)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider;

    protected UserAccount() {}

    private UserAccount(String userId,
                        String nickname,
                        String userPassword,
                        Gender gender,
                        String email,
                        int ageRange,
                        String profileImage,
                        Role role,
                        AuthProvider authProvider) {
        this.userId = userId;
        this.nickname = nickname;
        this.userPassword = userPassword;
        this.gender = gender;
        this.email = email;
        this.ageRange = ageRange;
        this.profileImageUrl = profileImage;
        this.role = role;
        this.authProvider = authProvider;
    }

    public static UserAccount of(String userId,
                                 String nickname,
                                 String userPassword,
                                 Gender gender,
                                 String email,
                                 int ageRange,
                                 String profileImage,
                                 Role role,
                                 AuthProvider authProvider) {
        return new UserAccount(userId, nickname, userPassword, gender, email, ageRange, profileImage, role, authProvider);
    }
}
