package com.ncookie.imad.domain.recommend.service;

import com.ncookie.imad.domain.contents.entity.Contents;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.useractivity.entity.UserActivity;
import com.ncookie.imad.domain.useractivity.service.UserActivityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
@Description("유저의 IMAD 활동 기반 작품 추천 관련 서비스")
// 작품 찜, 리뷰/게시글 좋아요, 스크랩 등을 기반으로 추천
public class UserActivityBasedRecommendationService {
    private final UserActivityService userActivityService;

    // 최근에 저장된 활동 기록의 작품일수록 더 높은 가중치를 가질 수 있도록 설정
    public double calculateWeight(LocalDateTime timestamp) {
        // 현재 시각과 데이터의 시각 차이를 계산
        long hoursDifference = Duration.between(timestamp, LocalDateTime.now()).toHours();

        // 지수 함수로 가중치를 계산한다. 최근 데이터일수록 가중치가 높아진다.
        return Math.exp(-hoursDifference / 24.0); // 하루를 기준으로 감쇄율을 설정
    }
    
    // 가중치가 설정된 작품들 중 랜덤으로 선택
    public Contents selectWeightedRandomContents(UserAccount user, ContentsType contentsType) {
        List<UserActivity> userActivityList = List.of();

        switch (contentsType) {
            case TV -> userActivityList = userActivityService.getUserActivityListWithTv(user);
            case MOVIE -> userActivityList = userActivityService.getUserActivityListWithMovie(user);
            case ANIMATION -> userActivityList = userActivityService.getUserActivityListWithAnimation(user);
        }
        
        // 유저 활동 기록이 없다면 null 반환
        if (userActivityList.isEmpty()) {
            return null;
        }

        // 각 작품의 가중치 계산
        List<Double> weights = userActivityList.stream()
                .map(userActivity -> calculateWeight(userActivity.getCreatedDate()))
                .toList();

        // 가중치 합계
        double totalWeight = weights.stream().mapToDouble(Double::doubleValue).sum();
        
        // 랜덤값 생성
        double randomValue = new Random().nextDouble() * totalWeight;

        // 가중치에 따라 작품 선택
        double cumulativeWeight = 0.0;
        for (int i = 0; i < weights.size(); i++) {
            cumulativeWeight += weights.get(i);
            if (cumulativeWeight >= randomValue) {
                return userActivityList.get(i).getContents();
            }
        }

        // 만약 어떤 이유로 선택되지 않았을 경우 (매우 낮은 확률) 가장 최근 활동 기록 작품 반환
        return userActivityList.get(userActivityList.size() - 1).getContents();
    }
}
