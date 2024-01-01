package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.ranking.entity.ContentsDailyRankingScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentsDailyScoreRankingRepository extends JpaRepository<ContentsDailyRankingScore, Long> {
    Optional<ContentsDailyRankingScore> findByContents(Contents contents);
}
