package com.ncookie.imad.domain.genre.repository;

import com.ncookie.imad.domain.genre.entity.UserPreferredGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferredGenreRepository extends JpaRepository<UserPreferredGenre, Long> {
}