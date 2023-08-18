package com.ncookie.imad.domain.season.repository;

import com.ncookie.imad.domain.season.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}