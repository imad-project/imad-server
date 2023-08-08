package com.ncookie.imad.global.openfeign;

import com.ncookie.imad.domain.contents.dto.SearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "tmdb", url = "${tmdb.api.url}")
public interface TmdbFeignClient {

    // 키워드 통합 검색(영화, TV, 시리즈, 인물 등 포함)
    @GetMapping("/search/multi")
    SearchResponse searchMultiByQuery(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestHeader(value = "Accept") String acceptHeader,
            @RequestParam(value = "query") String query,
            @RequestParam(value = "include_adult") boolean includeAdult,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );
}
