package com.ncookie.imad.domain.ranking.service;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.ranking.data.RankingPeriod;
import com.ncookie.imad.domain.ranking.dto.response.RankingDetailsResponse;
import com.ncookie.imad.domain.ranking.dto.response.RankingListResponse;
import com.ncookie.imad.domain.ranking.entity.RankingAllTime;
import com.ncookie.imad.domain.ranking.entity.RankingMonthly;
import com.ncookie.imad.domain.ranking.entity.RankingWeekly;
import com.ncookie.imad.global.Utils;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RankingSystemService {
    private final RankingRepositoryService rankingRepositoryService;


    public RankingListResponse getRankingList(RankingPeriod rankingPeriod, String contentsTypeString, int pageNumber) {
        // 현재 시각이 오전 12시부터 12시 5분 사이라면 랭킹 정보를 반환하지 않음
        if (isWithinUpdateTime()) {
            return null;
        }

        PageRequest rankingPageRequest = getRankingPageRequest(pageNumber);
        ContentsType contentsType = getContentsTypeByString(contentsTypeString);

        RankingListResponse rankingListResponse;
        switch (rankingPeriod) {
            case WEEKLY: {
                Page<RankingWeekly> weeklyRanking = rankingRepositoryService.getWeeklyRanking(rankingPageRequest, contentsType);
                rankingListResponse = RankingListResponse.toDTO(
                        weeklyRanking,
                        convertRankingWeeklyPageToRankingDetailsResponse(weeklyRanking.stream().toList(), contentsType)
                );
                break;
            }
            case MONTHLY: {
                Page<RankingMonthly> monthlyRanking = rankingRepositoryService.getMonthlyRanking(rankingPageRequest, contentsType);
                rankingListResponse = RankingListResponse.toDTO(
                        monthlyRanking,
                        convertRankingMonthlyPageToRankingDetailsResponse(monthlyRanking.stream().toList(), contentsType)
                );
                break;
            }
            case ALL_TIME: {
                Page<RankingAllTime> allTimeRanking = rankingRepositoryService.getAllTimeRanking(rankingPageRequest, contentsType);
                rankingListResponse = RankingListResponse.toDTO(
                        allTimeRanking,
                        convertRankingAllTimePageToRankingDetailsResponse(allTimeRanking.stream().toList(), contentsType)
                );
                break;
            }
            default: {
                throw new BadRequestException(ResponseCode.RANKING_WRONG_PERIOD);
            }
        }

        return rankingListResponse;
    }

    private boolean isWithinUpdateTime() {
        // 현재 시각 가져오기
        LocalTime currentTime = LocalTime.now();

        // 판단할 시간 범위 설정
        LocalTime startTime = LocalTime.of(0, 0); // 12시 0분
        LocalTime endTime = LocalTime.of(0, 5);   // 12시 5분

        return !currentTime.isBefore(startTime) && currentTime.isBefore(endTime);
    }

    private ContentsType getContentsTypeByString(String s) {
        ContentsType contentsType;

        try {
            contentsType  = ContentsType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("옳바르지 않은 contents type string으로 인해 에러 발생");
            log.error(e.getMessage());
            throw new BadRequestException(ResponseCode.RANKING_WRONG_CONTENTS_TYPE);
        }

        return contentsType;
    }

    private PageRequest getRankingPageRequest(int pageNumber) {
        Sort sort = Sort.by("ranking").ascending();
        return PageRequest.of(pageNumber, Utils.PAGE_SIZE, sort);
    }

    private List<RankingDetailsResponse> convertRankingWeeklyPageToRankingDetailsResponse(List<RankingWeekly> rankingList, ContentsType contentsType) {
        List<RankingDetailsResponse> rankingDetailsResponseList = new ArrayList<>();
        for (RankingWeekly ranking : rankingList) {
            rankingDetailsResponseList.add(RankingDetailsResponse.toDTO(ranking, contentsType));
        }

        return rankingDetailsResponseList;
    }

    private List<RankingDetailsResponse> convertRankingMonthlyPageToRankingDetailsResponse(List<RankingMonthly> rankingList, ContentsType contentsType) {
        List<RankingDetailsResponse> rankingDetailsResponseList = new ArrayList<>();
        for (RankingMonthly ranking : rankingList) {
            rankingDetailsResponseList.add(RankingDetailsResponse.toDTO(ranking, contentsType));
        }

        return rankingDetailsResponseList;
    }

    private List<RankingDetailsResponse> convertRankingAllTimePageToRankingDetailsResponse(List<RankingAllTime> rankingList, ContentsType contentsType) {
        List<RankingDetailsResponse> rankingDetailsResponseList = new ArrayList<>();
        for (RankingAllTime ranking : rankingList) {
            rankingDetailsResponseList.add(RankingDetailsResponse.toDTO(ranking, contentsType));
        }

        return rankingDetailsResponseList;
    }
}
