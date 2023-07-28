package com.ncookie.imad.domain.genre.repository;

import com.ncookie.imad.domain.genre.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}