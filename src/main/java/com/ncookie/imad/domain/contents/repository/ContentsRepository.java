package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
    @Query(value = "select test from Contents test where test.tmdbId = :id")
//    @Query(value = "select count (c.contentsId) > 0 from Contents c where c.tmdbId = :id",
//    nativeQuery = true)
    boolean checkValidation(@Param("id") long id);

    boolean existsByTmdbIdAndTmdbType(long id, ContentsType type);
}
