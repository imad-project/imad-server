package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.entity.MovieData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDataRepository extends JpaRepository<MovieData, Long> {
    MovieData findByTmdbIdAndTmdbType(long id, ContentsType type);
    MovieData findByContentsId(Long id);
}