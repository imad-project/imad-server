package com.ncookie.imad.domain.person.repository;

import com.ncookie.imad.domain.person.entity.Filmography;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmographyRepository extends JpaRepository<Filmography, Long> {
}