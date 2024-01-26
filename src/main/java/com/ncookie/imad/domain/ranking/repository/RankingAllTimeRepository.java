package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.ranking.entity.RankingAllTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingAllTimeRepository extends JpaRepository<RankingAllTime, Long> {
    Page<RankingAllTime> findAllByContentsType(Pageable pageable, ContentsType contentsType);
}
