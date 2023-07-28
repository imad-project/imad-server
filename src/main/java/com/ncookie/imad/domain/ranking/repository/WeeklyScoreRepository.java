package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.WeeklyScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyScoreRepository extends JpaRepository<WeeklyScore, Long> {
}