package com.ncookie.imad.domain.recommend.controller;

import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.recommend.data.RecommendCategory;
import com.ncookie.imad.domain.recommend.dto.response.*;
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
    public ApiResponse<AllRecommendationResponse> getAllRecommendations(@RequestHeader("Authorization") String accessToken) {
        return ApiResponse.createSuccess(
                ResponseCode.RECOMMEND_GET_SUCCESS,
                recommendationService.getAllRecommendations(accessToken)
        );
    }

    @Description("선호 장르 기반 추천")
    @GetMapping("/genre")
    public ApiResponse<GenreRecommendationResponse> getPreferredGenreRecommendations(@RequestHeader("Authorization") String accessToken,
                                                                                     @RequestParam("type") String type,
                                                                                     @RequestParam("page") int pageNumber) {
        return ApiResponse.createSuccess(
                ResponseCode.RECOMMEND_GET_SUCCESS,
                recommendationService.getPreferredGenreBasedRecommendation(accessToken, ContentsType.valueOf(type.toUpperCase()), pageNumber));
    }

    @Description("서비스 활동 기반 추천")
    @GetMapping("/activity")
    public ApiResponse<UserActivityRecommendationResponse> getUserActivityRecommendations(@RequestParam("page") int pageNumber,
                                                                                          @RequestParam("contents_id") Long contentsId) {
        return ApiResponse.createSuccess(
                ResponseCode.RECOMMEND_GET_SUCCESS,
                recommendationService.getUserActivityAdditionalRecommendation(pageNumber, contentsId)
        );
    }

    @Description("IMAD 추천")
    @GetMapping("/imad")
    public ApiResponse<ImadRecommendationResponse> getImadRecommendations(@RequestParam("type") String type,
                                                                          @RequestParam("page") int pageNumber,
                                                                          @RequestParam("category") String recommendCategory) {
        return ApiResponse.createSuccess(
                ResponseCode.RECOMMEND_GET_SUCCESS,
                recommendationService.getImadRecommendation(
                        ContentsType.valueOf(type.toUpperCase()),
                        pageNumber,
                        RecommendCategory.valueOf(recommendCategory.toUpperCase())
                )
        );
    }

    @Description("트렌드 작품 추천")
    @GetMapping("/trend")
    public ApiResponse<TrendRecommendationResponse> getTrendingRecommendations(@RequestParam("type") String type,
                                                                               @RequestParam("page") int pageNumber) {
        return ApiResponse.createSuccess(
                ResponseCode.RECOMMEND_GET_SUCCESS,
                recommendationService.getTrendRecommendation(ContentsType.valueOf(type.toUpperCase()), pageNumber));
    }
}
