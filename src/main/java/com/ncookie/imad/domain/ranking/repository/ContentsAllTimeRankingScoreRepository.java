package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.ranking.entity.ContentsAllTimeRankingScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentsAllTimeRankingScoreRepository extends JpaRepository<ContentsAllTimeRankingScore, Long> {
    Optional<ContentsAllTimeRankingScore> findByContents(Contents contents);
}