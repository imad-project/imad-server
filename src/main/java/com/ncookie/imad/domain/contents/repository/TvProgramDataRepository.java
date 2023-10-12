package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.TvProgramData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TvProgramDataRepository extends JpaRepository<TvProgramData, Long> {
    TvProgramData findByContentsId(Long id);
}