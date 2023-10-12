package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
    Contents findByTmdbIdAndTmdbType(Long id, ContentsType type);

    boolean existsByTmdbIdAndTmdbType(Long id, ContentsType type);
}
