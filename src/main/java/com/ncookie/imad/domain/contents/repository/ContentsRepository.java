package com.ncookie.imad.domain.contents.repository;

import com.ncookie.imad.domain.contents.entity.Contents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
}