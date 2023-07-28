package com.ncookie.imad.domain.posting.repository;

import com.ncookie.imad.domain.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting, Long> {
}