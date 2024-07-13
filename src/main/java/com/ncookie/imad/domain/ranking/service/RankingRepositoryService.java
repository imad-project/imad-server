package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.ranking.data.RankingPeriod;
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

    // ==============================================
    // 작품 타입별 랭킹 변화 트래킹용
    // ==============================================
    // 기존에 저장되어 있던 랭킹 데이터에서 이전 랭킹 순위 참조
    public Long getRankingNumberByContents(Contents contents, RankingPeriod rankingPeriod) {
        try {
            return switch (rankingPeriod) {
                case WEEKLY -> rankingWeeklyRepository.findByContents(contents).getRanking();
                case MONTHLY -> rankingMonthlyRepository.findByContents(contents).getRanking();
                case ALL_TIME -> rankingAllTimeRepository.findByContents(contents).getRanking();
            };
        } catch (Exception e) {
            return null;
        }
    }

    // 반환값이 null인 경우 : 작품이 랭킹에 신규 진입했을 때(Exception 발생)
    public Long getRankingNumberByTvContents(Contents contents, RankingPeriod rankingPeriod) {
        try {
            return switch (rankingPeriod) {
                case WEEKLY -> rankingWeeklyRepository.findByContents(contents).getRankingTv();
                case MONTHLY -> rankingMonthlyRepository.findByContents(contents).getRankingTv();
                case ALL_TIME -> rankingAllTimeRepository.findByContents(contents).getRankingTv();
            };
        } catch (Exception e) {
            return null;
        }
    }

    public Long getRankingNumberByMovieContents(Contents contents, RankingPeriod rankingPeriod) {
        try {
            return switch (rankingPeriod) {
                case WEEKLY -> rankingWeeklyRepository.findByContents(contents).getRankingMovie();
                case MONTHLY -> rankingMonthlyRepository.findByContents(contents).getRankingMovie();
                case ALL_TIME -> rankingAllTimeRepository.findByContents(contents).getRankingMovie();
            };
        } catch (Exception e) {
            return null;
        }
    }

    public Long getRankingNumberByAnimationContents(Contents contents, RankingPeriod rankingPeriod) {
        try {
            return switch (rankingPeriod) {
                case WEEKLY -> rankingWeeklyRepository.findByContents(contents).getRankingAnimation();
                case MONTHLY -> rankingMonthlyRepository.findByContents(contents).getRankingAnimation();
                case ALL_TIME -> rankingAllTimeRepository.findByContents(contents).getRankingAnimation();
            };
        } catch (Exception e) {
            return null;
        }
    }
}
