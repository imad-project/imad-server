package com.ncookie.imad.domain.ranking.repository;

import com.ncookie.imad.domain.posting.entity.Posting;
import com.ncookie.imad.domain.ranking.entity.TodayPopularPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TodayPopularPostingsRepository extends JpaRepository<TodayPopularPosting, Posting> {
    Optional<TodayPopularPosting> findByPosting(Posting posting);

    @Query("SELECT pp FROM TodayPopularPosting pp " +
            "WHERE pp.popularScore = (SELECT MAX(pp2.popularScore) FROM TodayPopularPosting pp2)")
    List<TodayPopularPosting> findTopByPopularScore();
}
