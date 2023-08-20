package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
    Contents findByTmdbIdAndTmdbType(long id, ContentsType type);

    boolean existsByTmdbIdAndTmdbType(long id, ContentsType type);
}
