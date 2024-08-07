package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.ranking.entity.RankingMonthly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingMonthlyRepository extends JpaRepository<RankingMonthly, Long> {
    Page<RankingMonthly> findAllByContentsType(Pageable pageable, ContentsType contentsType);
    RankingMonthly findByContents(Contents contents);
}
