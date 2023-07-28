package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.MovieData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDataRepository extends JpaRepository<MovieData, Long> {
}