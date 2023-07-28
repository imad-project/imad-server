package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.AllTimeRanking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllTimeRankingRepository extends JpaRepository<AllTimeRanking, Long> {
}