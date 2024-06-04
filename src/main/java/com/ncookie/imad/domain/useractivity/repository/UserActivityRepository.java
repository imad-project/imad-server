package com.ncookie.imad.domain.useractivity.repository;

import com.ncookie.imad.domain.useractivity.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
}