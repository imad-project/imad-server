package com.ncookie.imad.domain.ranking.controller;

import com.ncookie.imad.domain.ranking.data.RankingPeriod;
import com.ncookie.imad.domain.ranking.dto.response.RankingListResponse;
import com.ncookie.imad.domain.ranking.service.RankingSystemService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ranking")
public class RankingController {
    private final RankingSystemService rankingSystemService;

    @Description("주간 랭킹 조회")
    @GetMapping("/weekly/{contents_type}")
    public ApiResponse<RankingListResponse> rankingGetWeekly(@PathVariable("contents_type") String contentsTypeString) {
        RankingListResponse rankingList = rankingSystemService.getRankingList(RankingPeriod.WEEKLY, contentsTypeString);

        return ApiResponse.createSuccess(ResponseCode.RANKING_GET_SUCCESS, rankingList);
    }

    @Description("월간 랭킹 조회")
    @GetMapping("/monthly/{contents_type}")
    public ApiResponse<RankingListResponse> rankingGetMonthly(@PathVariable("contents_type") String contentsTypeString) {
        RankingListResponse rankingList = rankingSystemService.getRankingList(RankingPeriod.MONTHLY, contentsTypeString);

        return ApiResponse.createSuccess(ResponseCode.RANKING_GET_SUCCESS, rankingList);
    }

    @Description("전체 랭킹 조회")
    @GetMapping("/alltime/{contents_type}")
    public ApiResponse<RankingListResponse> rankingGetAllTime(@PathVariable("contents_type") String contentsTypeString) {
        RankingListResponse rankingList = rankingSystemService.getRankingList(RankingPeriod.ALL_TIME, contentsTypeString);

        return ApiResponse.createSuccess(ResponseCode.RANKING_GET_SUCCESS, rankingList);
    }
}
