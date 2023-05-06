package com.ncookie.imad.repository;

import com.ncookie.imad.domain.UserAccount;
import com.ncookie.imad.domain.type.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    Optional<UserAccount> findByEmail(String email);

    boolean existsByUserIdAndAuthProvider(String id, AuthProvider authProvider);
}
