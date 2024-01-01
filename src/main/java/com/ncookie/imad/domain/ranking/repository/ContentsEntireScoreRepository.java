package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.ranking.entity.ContentsEntireScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentsEntireScoreRepository extends JpaRepository<ContentsEntireScore, Long> {
    Optional<ContentsEntireScore> findByContents(Contents contents);
}