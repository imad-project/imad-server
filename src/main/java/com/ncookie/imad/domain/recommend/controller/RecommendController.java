package com.ncookie.imad.domain.recommend.controller;

import com.ncookie.imad.domain.recommend.service.ContentsRecommendationService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {
    private final ContentsRecommendationService recommendationService;
    
    @Description("선호 장르 기반 추천")
    @GetMapping("/genre")
    public ApiResponse<?> recommendBasedPreferredGenre() {

        return null;
    }
}
