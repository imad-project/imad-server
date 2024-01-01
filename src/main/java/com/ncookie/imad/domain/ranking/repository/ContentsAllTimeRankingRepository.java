package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.ContentsAllTimeRanking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsAllTimeRankingRepository extends JpaRepository<ContentsAllTimeRanking, Long> {
}