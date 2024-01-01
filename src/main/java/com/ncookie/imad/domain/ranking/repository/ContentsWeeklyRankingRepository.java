package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.ContentsWeeklyRanking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsWeeklyRankingRepository extends JpaRepository<ContentsWeeklyRanking, Long> {
}