package com.ncookie.imad.domain.user.repository;

import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.entity.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    Optional<UserAccount> findByEmail(String email);

    boolean existsByUserIdAndAuthProvider(String id, AuthProvider authProvider);
}
