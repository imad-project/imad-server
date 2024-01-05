package com.ncookie.imad.domain.ranking.controller;

import com.ncookie.imad.domain.ranking.data.RankingPeriod;
import com.ncookie.imad.domain.ranking.dto.ContentsData;
import com.ncookie.imad.domain.ranking.dto.response.RankingInfo;
import com.ncookie.imad.domain.ranking.service.RankingSystemService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ranking")
public class RankingController {
    private final RankingSystemService rankingSystemService;

    @Description("주간 랭킹 조회")
    @GetMapping("/weekly/{contents_type}")
    public ApiResponse<RankingInfo> rankingGetWeekly(@PathVariable("contents_type") String contentsTypeString) {
        Set<ContentsData> rankingData = rankingSystemService.getRankingByContentsType(RankingPeriod.WEEKLY, contentsTypeString);

        if (rankingData == null) {
            log.info("랭킹 데이터가 존재하지 않습니다.");
            return ApiResponse.createSuccess(ResponseCode.RANKING_GET_NO_DATA, null);
        }

        RankingInfo rankingInfo = RankingInfo.toDTO(rankingData);
        return ApiResponse.createSuccess(ResponseCode.RANKING_GET_SUCCESS, rankingInfo);
    }

    @Description("월간 랭킹 조회")
    @GetMapping("/monthly/{contents_type}")
    public ApiResponse<RankingInfo> rankingGetMonthly(@PathVariable("contents_type") String contentsTypeString) {
        Set<ContentsData> rankingData = rankingSystemService.getRankingByContentsType(RankingPeriod.MONTHLY, contentsTypeString);

        if (rankingData == null) {
            log.info("랭킹 데이터가 존재하지 않습니다.");
            return ApiResponse.createSuccess(ResponseCode.RANKING_GET_NO_DATA, null);
        }

        RankingInfo rankingInfo = RankingInfo.toDTO(rankingData);
        return ApiResponse.createSuccess(ResponseCode.RANKING_GET_SUCCESS, rankingInfo);
    }

    @Description("전체 랭킹 조회")
    @GetMapping("/alltime/{contents_type}")
    public ApiResponse<RankingInfo> rankingGetAllTime(@PathVariable("contents_type") String contentsTypeString) {
        Set<ContentsData> rankingData = rankingSystemService.getRankingByContentsType(RankingPeriod.ALL_TIME, contentsTypeString);

        if (rankingData == null) {
            log.info("랭킹 데이터가 존재하지 않습니다.");
            return ApiResponse.createSuccess(ResponseCode.RANKING_GET_NO_DATA, null);
        }

        RankingInfo rankingInfo = RankingInfo.toDTO(rankingData);
        return ApiResponse.createSuccess(ResponseCode.RANKING_GET_SUCCESS, rankingInfo);
    }
}
