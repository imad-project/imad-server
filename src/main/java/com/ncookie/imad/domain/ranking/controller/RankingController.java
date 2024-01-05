package com.ncookie.imad.domain.ranking.controller;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.ranking.dto.RankingInfo;
import com.ncookie.imad.domain.ranking.service.RankingSystemService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
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

        RankingInfo rankingInfo = RankingInfo.toDTO(
                rankingSystemService.getRankingByContentsType("alltime", contentsType)
        );

        return ApiResponse.createSuccess(ResponseCode.RANKING_GET_SUCCESS, rankingInfo);
    }
}
