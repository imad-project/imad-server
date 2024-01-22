package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.RankingAllTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingAllTimeRepository extends JpaRepository<RankingAllTime, Long> {
}
