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

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RankingSystemService {
    private final RankingRepositoryService rankingRepositoryService;


    public RankingListResponse getRankingList(RankingPeriod rankingPeriod, String contentsTypeString, int pageNumber) {
        PageRequest rankingPageRequest = getRankingPageRequest(pageNumber);
        ContentsType contentsType = getContentsTypeByString(contentsTypeString);

        RankingListResponse rankingListResponse;
        switch (rankingPeriod) {
            case WEEKLY: {
                Page<RankingWeekly> weeklyRanking = rankingRepositoryService.getWeeklyRanking(rankingPageRequest, contentsType);
                rankingListResponse = RankingListResponse.toDTO(
                        weeklyRanking,
                        convertRankingWeeklyPageToRankingDetailsResponse(weeklyRanking.stream().toList())
                );
                break;
            }
            case MONTHLY: {
                Page<RankingMonthly> monthlyRanking = rankingRepositoryService.getMonthlyRanking(rankingPageRequest, contentsType);
                rankingListResponse = RankingListResponse.toDTO(
                        monthlyRanking,
                        convertRankingMonthlyPageToRankingDetailsResponse(monthlyRanking.stream().toList())
                );
                break;
            }
            case ALL_TIME: {
                Page<RankingAllTime> allTimeRanking = rankingRepositoryService.getAllTimeRanking(rankingPageRequest, contentsType);
                rankingListResponse = RankingListResponse.toDTO(
                        allTimeRanking,
                        convertRankingAllTimePageToRankingDetailsResponse(allTimeRanking.stream().toList())
                );
                break;
            }
            default: {
                throw new BadRequestException(ResponseCode.RANKING_WRONG_PERIOD);
            }
        }

        return rankingListResponse;
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

    private List<RankingDetailsResponse> convertRankingWeeklyPageToRankingDetailsResponse(List<RankingWeekly> rankingList) {
        List<RankingDetailsResponse> rankingDetailsResponseList = new ArrayList<>();
        for (RankingWeekly ranking : rankingList) {
            rankingDetailsResponseList.add(RankingDetailsResponse.toDTO(ranking));
        }

        return rankingDetailsResponseList;
    }

    private List<RankingDetailsResponse> convertRankingMonthlyPageToRankingDetailsResponse(List<RankingMonthly> rankingList) {
        List<RankingDetailsResponse> rankingDetailsResponseList = new ArrayList<>();
        for (RankingMonthly ranking : rankingList) {
            rankingDetailsResponseList.add(RankingDetailsResponse.toDTO(ranking));
        }

        return rankingDetailsResponseList;
    }

    private List<RankingDetailsResponse> convertRankingAllTimePageToRankingDetailsResponse(List<RankingAllTime> rankingList) {
        List<RankingDetailsResponse> rankingDetailsResponseList = new ArrayList<>();
        for (RankingAllTime ranking : rankingList) {
            rankingDetailsResponseList.add(RankingDetailsResponse.toDTO(ranking));
        }

        return rankingDetailsResponseList;
    }

}
