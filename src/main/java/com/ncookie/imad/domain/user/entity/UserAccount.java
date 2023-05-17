package com.ncookie.imad.domain.user.entity;

import com.ncookie.imad.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "user_id"),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "nickname", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserAccount extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Setter private String email;               // 이메일
    @Setter private String password;

    @Setter @Column(length = 50)  private String nickname;
    @Setter private Gender gender;              // 성별, 0 : 남자, 1 : 여자
    @Setter private int ageRange;               // 연령대
    @Setter private int profileImage;     // 프로필 이미지(URL)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider;

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)
    private String refreshToken;

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = Role.USER;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
