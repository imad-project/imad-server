package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.ranking.entity.RankingWeekly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingWeeklyRepository extends JpaRepository<RankingWeekly, Long> {
    Page<RankingWeekly> findAllByContentsType(Pageable pageable, ContentsType contentsType);
    RankingWeekly findByContents(Contents contents);
}
