package com.ncookie.imad.domain.ranking.controller;

import com.ncookie.imad.domain.ranking.service.RankingSystemService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ranking")
public class RankingController {
    private final RankingSystemService rankingSystemService;

    @Description("랭킹 조회")
    @GetMapping("")
    public ApiResponse<?> rankingGet() {
        return ApiResponse.createSuccessWithNoContent(
                ResponseCode.RANKING_GET_SUCCESS);
    }
}
