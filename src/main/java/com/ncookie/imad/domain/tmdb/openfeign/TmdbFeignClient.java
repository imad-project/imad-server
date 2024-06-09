package com.ncookie.imad.domain.tmdb.openfeign;

import com.ncookie.imad.domain.contents.dto.ContentsSearchResponse;
import com.ncookie.imad.domain.tmdb.dto.TmdbDetails;
import com.ncookie.imad.domain.tmdb.dto.TmdbDiscoverMovie;
import com.ncookie.imad.domain.tmdb.dto.TmdbDiscoverTv;
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
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "append_to_response") String appendToResponse
    );

    @GetMapping("/movie/{id}")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDetails getMovieDetailsById(
            @RequestHeader(value = "Authorization") String apiKey,
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "append_to_response") String appendToResponse
    );

    @GetMapping("/tv/{id}/content_ratings")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    String getTvCertification(
            @RequestHeader(value = "Authorization") String apiKey,
            @PathVariable(value = "id") Long id
    );

    @GetMapping("/movie/{id}/release_dates")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    String getMovieCertification(
            @RequestHeader(value = "Authorization") String apiKey,
            @PathVariable(value = "id") Long id
    );

    @GetMapping("/discover/tv")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDiscoverTv discoverTvWithPreferredGenre(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "include_adult") boolean includeAdult,
            @RequestParam(value = "include_null_first_air_dates") boolean includeNullFirstAirDates,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "sort_by") String sortBy,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "with_genres") String genres
    );

    @GetMapping("/discover/movie")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDiscoverMovie discoverMovieWithPreferredGenre(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "include_adult") boolean includeAdult,
            @RequestParam(value = "include_null_first_air_dates") boolean includeNullFirstAirDates,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "sort_by") String sortBy,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "with_genres") String genres
    );

    @GetMapping("/tv/{id}/similar")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDiscoverTv getSimilarTVs(
            @RequestHeader(value = "Authorization") String apiKey,
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );

    @GetMapping("/movie/{id}/similar")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDiscoverMovie getSimilarMovies(
            @RequestHeader(value = "Authorization") String apiKey,
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );

    @GetMapping("/tv/popular")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDiscoverTv getPopularTVs(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );

    @GetMapping("/movie/popular")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDiscoverMovie getPopularMovies(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );

    @GetMapping("/tv/top_rated")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDiscoverTv getTopRatedTVs(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );

    @GetMapping("/movie/top_rated")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDiscoverMovie getTopRatedMovies(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );

    @GetMapping("/trending/tv/day")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDiscoverTv getTrendingTVs(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );

    @GetMapping("/trending/movie/day")
    @Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
    TmdbDiscoverMovie getTrendingMovies(
            @RequestHeader(value = "Authorization") String apiKey,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "page") int page
    );
}
