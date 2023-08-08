package com.ncookie.imad.global.openfeign;

import com.ncookie.imad.domain.contents.dto.SearchResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "tmdb", url = "${tmdb.api.api-url}")
public interface TmdbFeignClient {

    // 키워드 통합 검색(영화, TV, 시리즈, 인물 등 포함)
    @GetMapping(value = "/search/multi")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    SearchResponse searchMultiByQuery(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "query") String query,
            @RequestParam(value = "include_adult") boolean includeAdult,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );
}
