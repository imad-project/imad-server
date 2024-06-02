package com.ncookie.imad.domain.recommend.controller;

import com.ncookie.imad.domain.recommend.dto.response.ContentsRecommendationResponse;
import com.ncookie.imad.domain.recommend.service.ContentsRecommendationService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {
    private final ContentsRecommendationService recommendationService;

    @Description("선호 장르 기반 추천")
    @GetMapping("/genre")
    public ApiResponse<ContentsRecommendationResponse> recommendBasedPreferredGenre(@RequestHeader("Authorization") String accessToken,
                                                                                    @RequestParam("page") int pageNumber) {
        recommendationService.getImadRecommendation(accessToken, pageNumber);
        return ApiResponse.createSuccess(
                ResponseCode.RECOMMEND_GET_SUCCESS,
                recommendationService.getPreferredGenreBasedRecommendation(accessToken, pageNumber));
    }
}
