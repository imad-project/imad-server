package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}