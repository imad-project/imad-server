package com.ncookie.imad.domain.season.repository;

import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.season.entity.SeasonCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeasonCollectionRepository extends JpaRepository<SeasonCollection, Long> {
    List<SeasonCollection> findAllByTvProgramData(TvProgramData tvProgramData);
}
