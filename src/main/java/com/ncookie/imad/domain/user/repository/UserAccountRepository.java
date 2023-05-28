package com.ncookie.imad.domain.user.repository;

import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.entity.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByNickname(String nickname);
    Optional<UserAccount> findByRefreshToken(String refreshToken);

    /*
     * 일반 유저의 경우 직접 입력한 id, 소셜 유저의 경우 provider에서 제공받은 id와
     * auth provider를 함께 식별자로 사용함
     */
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<UserAccount> findByAuthProviderAndSocialId(AuthProvider authProvider, String socialId);
    Optional<UserAccount> findByIdAndEmail(Long id, String email);
}
