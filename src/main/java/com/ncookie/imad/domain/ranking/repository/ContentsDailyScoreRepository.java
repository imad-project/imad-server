package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.ranking.entity.ContentsDailyScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsDailyScoreRepository extends JpaRepository<ContentsDailyScore, Long> {
}
