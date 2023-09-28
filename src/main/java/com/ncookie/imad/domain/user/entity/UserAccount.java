package com.ncookie.imad.domain.user.entity;

import com.ncookie.imad.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Table(indexes = {
        @Index(columnList = "user_id"),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "nickname", unique = true)
})
@Builder
@Getter
@ToString
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

    // 유저 선호 장르
    @Setter
    @ElementCollection
    @CollectionTable(name = "preferred_genres", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    @Builder.Default
    private Set<Long> preferredGenres = new HashSet<>();

    private String socialId;            // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)
    @Setter private String oauth2AccessToken;   // oauth2 업체와의 연결 해제, 발급받은 토큰 revoke 등을 처리할 때 사용
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
