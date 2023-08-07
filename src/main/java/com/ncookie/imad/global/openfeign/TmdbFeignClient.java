package com.ncookie.imad.global.openfeign;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tmdb", url = "https://api.themoviedb.org/3")
public interface TmdbFeignClient {

    @GetMapping("/search/multi")
    Response searchByKeywords(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestHeader(value = "accept") String acceptHeader,
            @RequestParam(value = "query") String keyword,
            @RequestParam(value = "include_adult", defaultValue = "true", required = false) boolean includeAdult,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );
}
