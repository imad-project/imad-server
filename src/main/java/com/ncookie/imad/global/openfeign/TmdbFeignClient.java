package com.ncookie.imad.global.openfeign;

import com.ncookie.imad.domain.contents.dto.ContentsSearchResponse;
import com.ncookie.imad.domain.tmdb.dto.TmdbDetails;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "tmdb", url = "${tmdb.api.api-url}")
public interface TmdbFeignClient {

    // 키워드 통합 검색(영화, TV, 시리즈, 인물 등 포함)
    @GetMapping(value = "/search/multi")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    ContentsSearchResponse searchMultiByQuery(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "query") String query,
            @RequestParam(value = "include_adult") boolean includeAdult,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );

    @GetMapping(value = "/search/tv")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    ContentsSearchResponse searchTvByQuery(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "query") String query,
            @RequestParam(value = "include_adult") boolean includeAdult,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );

    @GetMapping(value = "/search/movie")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    ContentsSearchResponse searchMovieByQuery(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "query") String query,
            @RequestParam(value = "include_adult") boolean includeAdult,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );

    @GetMapping("/tv/{id}")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDetails getTvDetailsById(
            @RequestHeader(value = "Authorization") String apiKey,
            @PathVariable(value = "id") int id,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "append_to_response") String appendToResponse
    );

    @GetMapping("/movie/{id}")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDetails getMovieDetailsById(
            @RequestHeader(value = "Authorization") String apiKey,
            @PathVariable(value = "id") int id,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "append_to_response") String appendToResponse
    );

    @GetMapping("/tv/{id}/content_ratings")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    String getTvCertification(
            @RequestHeader(value = "Authorization") String apiKey,
            @PathVariable(value = "id") int id
    );

    @GetMapping("/movie/{id}/release_dates")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    String getMovieCertification(
            @RequestHeader(value = "Authorization") String apiKey,
            @PathVariable(value = "id") int id
    );
}
