package com.ncookie.imad.domain.recommend.service;

import org.springframework.context.annotation.Description;

@Description("유저의 선호 장르 기반 작품 추천 관련 서비스")
public class PreferredGenreBasedRecommendationService {
    // 아래 링크에서 조회 가능
    // https://api.themoviedb.org/3/discover/movie
    // ?include_adult=false&include_video=false&language=ko-kr&page=1&sort_by=popularity.desc&with_genres=12%2C%2014

    // TODO: `discover`의 검색 결과는 details 또는 search 와는 반환값이 다르기 때문에 DTO 클래스를 별도로 만들어야 한다.
}
