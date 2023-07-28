package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.AllTimeScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllTimeScoreRepository extends JpaRepository<AllTimeScore, Long> {
}