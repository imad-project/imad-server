package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.ranking.entity.TodayPopularPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodayPopularPostingsRepository extends JpaRepository<TodayPopularPosting, Posting> {
    Optional<TodayPopularPosting> findByPosting(Posting posting);
}