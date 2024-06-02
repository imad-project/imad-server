package com.ncookie.imad.domain.recommend.controller;

import com.ncookie.imad.domain.recommend.dto.response.AllRecommendationResponse;
import com.ncookie.imad.domain.recommend.dto.response.GenreRecommendationResponse;
import com.ncookie.imad.domain.recommend.dto.response.ImadRecommendationResponse;
import com.ncookie.imad.domain.recommend.dto.response.TrendRecommendationResponse;
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

    @Description("작품 추천 종합 버전")
    @GetMapping("/all")
    public ApiResponse<AllRecommendationResponse> getAllRecommendations(@RequestHeader("Authorization") String accessToken,
                                                                        @RequestParam("page") int pageNumber) {
        return ApiResponse.createSuccess(
                ResponseCode.RECOMMEND_GET_SUCCESS,
                recommendationService.getAllRecommendations(accessToken, pageNumber)
        );
    }

    @Description("선호 장르 기반 추천")
    @GetMapping("/genre")
    public ApiResponse<GenreRecommendationResponse> getPreferredGenreRecommendations(@RequestHeader("Authorization") String accessToken,
                                                                                 @RequestParam("page") int pageNumber) {
        return ApiResponse.createSuccess(
                ResponseCode.RECOMMEND_GET_SUCCESS,
                recommendationService.getPreferredGenreBasedRecommendation(accessToken, pageNumber));
    }

    @Description("IMAD 추천")
    @GetMapping("/imad")
    public ApiResponse<ImadRecommendationResponse> getImadRecommendations(@RequestParam("page") int pageNumber) {
        return ApiResponse.createSuccess(
                ResponseCode.RECOMMEND_GET_SUCCESS,
                recommendationService.getImadRecommendation(pageNumber));
    }

    @Description("트렌드 작품 추천")
    @GetMapping("/trend")
    public ApiResponse<TrendRecommendationResponse> getTrendingRecommendations(@RequestParam("page") int pageNumber) {
        return ApiResponse.createSuccess(
                ResponseCode.RECOMMEND_GET_SUCCESS,
                recommendationService.getTrendRecommendation(pageNumber));
    }
}
