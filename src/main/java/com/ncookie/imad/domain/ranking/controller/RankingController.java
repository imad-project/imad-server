package com.ncookie.imad.domain.ranking.controller;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.ranking.dto.ContentsData;
import com.ncookie.imad.domain.ranking.dto.RankingInfo;
import com.ncookie.imad.domain.ranking.service.RankingSystemService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
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

    @Description("랭킹 조회")
    @GetMapping("/alltime/{contents_type}")
    public ApiResponse<RankingInfo> rankingGetAllTime(@PathVariable("contents_type") String type) {
        ContentsType contentsType;
        try {
            contentsType  = ContentsType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("옳바르지 않은 contents type string으로 인해 에러 발생");
            log.error(e.getMessage());
            throw new BadRequestException(ResponseCode.RANKING_WRONG_CONTENTS_TYPE);
        }

        Set<ContentsData> rankingData = rankingSystemService.getRankingByContentsType("alltime", contentsType);
        log.info("랭킹 서비스에서 데이터 얻어옴");

        if (rankingData == null) {
            log.info("랭킹 데이터가 존재하지 않습니다.");
            return ApiResponse.createSuccess(ResponseCode.RANKING_GET_NO_DATA, null);
        }

        RankingInfo rankingInfo = RankingInfo.toDTO(rankingData);
        return ApiResponse.createSuccess(ResponseCode.RANKING_GET_SUCCESS, rankingInfo);
    }
}
