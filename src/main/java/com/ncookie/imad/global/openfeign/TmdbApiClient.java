package com.ncookie.imad.global.openfeign;

import com.ncookie.imad.domain.contents.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

// TMDB API를 사용하기 위한 메소드들을 모아놓은 클래스
@EnableConfigurationProperties({ TmdbApiProperties.class })
@RequiredArgsConstructor
@Component
public class TmdbApiClient {
    private final TmdbFeignClient feignClient;
    private final TmdbApiProperties apiProperties;

    private final String language = "ko-kr";

    public SearchResponse searchMultiByQuery(String query) {
        return feignClient.searchMultiByQuery(
                apiProperties.getApiKey(),
                query,
                true,
                language,
                1
        );
    }
}
