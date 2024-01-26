package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.ranking.entity.RankingAllTime;
import com.ncookie.imad.domain.ranking.entity.RankingMonthly;
import com.ncookie.imad.domain.ranking.entity.RankingWeekly;
import com.ncookie.imad.domain.ranking.repository.RankingAllTimeRepository;
import com.ncookie.imad.domain.ranking.repository.RankingMonthlyRepository;
import com.ncookie.imad.domain.ranking.repository.RankingWeeklyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RankingRepositoryService {
    private final RankingWeeklyRepository rankingWeeklyRepository;
    private final RankingMonthlyRepository rankingMonthlyRepository;
    private final RankingAllTimeRepository rankingAllTimeRepository;


    // ==============================================
    // 주간
    // ==============================================
    public void rankingWeeklyDeleteAll() {
        rankingWeeklyRepository.deleteAllInBatch();
    }

    public void rankingWeeklySaveAll(List<RankingWeekly> rankingWeeklyList) {
        rankingWeeklyRepository.saveAll(rankingWeeklyList);
    }

    public Page<RankingWeekly> getWeeklyRanking(Pageable pageable, ContentsType contentsType) {
        if (contentsType.equals(ContentsType.ALL)) {
            return rankingWeeklyRepository.findAll(pageable);
        } else {
            return rankingWeeklyRepository.findAllByContentsType(pageable, contentsType);
        }
    }

    // ==============================================
    // 월간
    // ==============================================
    public void rankingMonthlyDeleteAll() {
        rankingMonthlyRepository.deleteAllInBatch();
    }

    public void rankingMonthlySaveAll(List<RankingMonthly> rankingWeeklyList) {
        rankingMonthlyRepository.saveAll(rankingWeeklyList);
    }

    public Page<RankingMonthly> getMonthlyRanking(Pageable pageable, ContentsType contentsType) {
        if (contentsType.equals(ContentsType.ALL)) {
            return rankingMonthlyRepository.findAll(pageable);
        } else {
            return rankingMonthlyRepository.findAllByContentsType(pageable, contentsType);
        }
    }

    // ==============================================
    // 전체
    // ==============================================
    public void rankingAllTimeDeleteAll() {
        rankingAllTimeRepository.deleteAllInBatch();
    }

    public void rankingAllTimeSaveAll(List<RankingAllTime> rankingWeeklyList) {
        rankingAllTimeRepository.saveAll(rankingWeeklyList);
    }

    public Page<RankingAllTime> getAllTimeRanking(Pageable pageable, ContentsType contentsType) {
        if (contentsType.equals(ContentsType.ALL)) {
            return rankingAllTimeRepository.findAll(pageable);
        } else {
            return rankingAllTimeRepository.findAllByContentsType(pageable, contentsType);
        }
    }
}
