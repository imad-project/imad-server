package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.WeeklyRanking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyRankingRepository extends JpaRepository<WeeklyRanking, Long> {
}