package com.ncookie.imad.domain.genre.repository;

import com.ncookie.imad.domain.genre.entity.ContentsGenreDeprecated;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsGenreRepository extends JpaRepository<ContentsGenreDeprecated, Long> {
}