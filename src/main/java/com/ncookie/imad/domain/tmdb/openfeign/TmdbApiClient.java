package com.ncookie.imad.domain.tmdb.openfeign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.contents.dto.ContentsSearchResponse;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.tmdb.dto.TmdbDetails;
import com.ncookie.imad.domain.tmdb.dto.TmdbDiscoverMovie;
import com.ncookie.imad.domain.tmdb.dto.TmdbDiscoverTv;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;


// TMDB API를 사용하기 위한 메소드들을 모아놓은 클래스
@EnableConfigurationProperties({ TmdbApiProperties.class })
@RequiredArgsConstructor
@Component
public class TmdbApiClient {
    private final TmdbFeignClient feignClient;
    private final TmdbApiProperties apiProperties;

    private final String LANGUAGE_STRING = "ko-kr";
    private final List<String> appendResponseForDetails = new ArrayList<>(Arrays.asList("credits", "images", "videos"));


    // 쿼리로 작품 검색. 전체 / TV / 영화 별로 검색할 수 있음
    // TMDB API에서 page는 1부터 시작함
    public ContentsSearchResponse searchByQuery(String query, String type, int page) {
        return switch (type) {
            case "multi" -> feignClient.searchMultiByQuery(apiProperties.getApiKey(), query, false, LANGUAGE_STRING, page);
            case "tv" -> feignClient.searchTvByQuery(apiProperties.getApiKey(), query, false, LANGUAGE_STRING, page);
            case "movie" -> feignClient.searchMovieByQuery(apiProperties.getApiKey(), query, false, LANGUAGE_STRING, page);
            default -> throw new BadRequestException(ResponseCode.CONTENTS_SEARCH_WRONG_TYPE);
        };
    }

    // 작품 상세 정보 조회
    public TmdbDetails fetchContentsDetails(Long id, ContentsType type) {
        try {

            if (type.equals(ContentsType.TV)) {
                return feignClient.getTvDetailsById(
                        apiProperties.getApiKey(),
                        id,
                        LANGUAGE_STRING,
                        listStringToCommaSeparatedString(appendResponseForDetails));
            } else if (type.equals(ContentsType.MOVIE)) {
                return feignClient.getMovieDetailsById(
                        apiProperties.getApiKey(),
                        id,
                        LANGUAGE_STRING,
                        listStringToCommaSeparatedString(appendResponseForDetails));
            } else {
                throw new BadRequestException(ResponseCode.CONTENTS_SEARCH_WRONG_TYPE);
            }
        } catch (FeignException e) {
            throw new BadRequestException(ResponseCode.CONTENTS_NOT_EXIST_TMDB_ID);
        }
    }

    public String fetchContentsCertification(Long id, ContentsType type) {
        ObjectMapper objectMapper = new ObjectMapper();
        String certification = "";
        
        // TV와 MOVIE의 시청등급을 얻어오는 방법이 다름
        try {
            if (type.equals(ContentsType.TV)) {
                JsonNode rootNode = objectMapper.readTree(
                        feignClient.getTvCertification(apiProperties.getApiKey(), id));
                JsonNode resultsNode = rootNode.get("results");

                for (JsonNode resultNode : resultsNode) {
                    JsonNode isoNode = resultNode.get("iso_3166_1");
                    if (isoNode != null && "KR".equals(isoNode.asText())) {
                        certification = resultNode.get("rating").asText();
                        break;
                    }
                }

            } else if (type.equals(ContentsType.MOVIE)) {
                JsonNode rootNode = objectMapper.readTree(
                        feignClient.getMovieCertification(apiProperties.getApiKey(), id));
                JsonNode resultsNode = rootNode.get("results");

                firstLoop:  // 중첩 for문을 탈출하기 위한 라벨
                for (JsonNode resultNode : resultsNode) {
                    JsonNode isoNode = resultNode.get("iso_3166_1");
                    if (isoNode != null && "KR".equals(isoNode.asText())) {
                        JsonNode releaseDatesNode = resultNode.get("release_dates");
                        if (releaseDatesNode != null && releaseDatesNode.isArray()) {
                            for (JsonNode releaseDateNode : releaseDatesNode) {
                                JsonNode certificationNode = releaseDateNode.get("certification");
                                if (certificationNode != null) {
                                    certification = certificationNode.asText();
                                    break firstLoop;
                                }
                            }
                        }
                    }
                }
            } else {
                throw new BadRequestException(ResponseCode.CONTENTS_SEARCH_WRONG_TYPE);
            }
        } catch (JsonProcessingException e) {
            throw new BadRequestException(ResponseCode.CONTENTS_PARSING_CERTIFICATION_ERROR);
        }

        return certification.isEmpty() ? "NONE" : certification;
    }

    public TmdbDiscoverTv discoverTvWithPreferredGenre(int pageNumber, Set<Long> genres) {
        return feignClient.discoverTvWithPreferredGenre(
                apiProperties.getApiKey(),
                false,
                false,
                LANGUAGE_STRING,
                "popularity.desc",
                pageNumber,
                listStringToVerticalBarSeparatedString(genres)
        );
    }

    public TmdbDiscoverMovie discoverMovieWithPreferredGenre(int pageNumber, Set<Long> genres) {
        return feignClient.discoverMovieWithPreferredGenre(
                apiProperties.getApiKey(),
                false,
                false,
                LANGUAGE_STRING,
                "popularity.desc",
                pageNumber,
                listStringToVerticalBarSeparatedString(genres)
        );
    }

    // TODO: TMDB의 Trend, Popular, Top Rated와 같이 갱신이 빈번하지 않은 데이터의 경우
    // 기본적으로 로컬에 데이터를 저장해뒀다가 주기적으로 업데이트한다.
    public TmdbDiscoverTv fetchTmdbPopularTv(int pageNumber) {
        return feignClient.getPopularTVs(
                apiProperties.getApiKey(),
                LANGUAGE_STRING,
                pageNumber
        );
    }

    public TmdbDiscoverMovie fetchTmdbPopularMovie(int pageNumber) {
        return feignClient.getPopularMovies(
                apiProperties.getApiKey(),
                LANGUAGE_STRING,
                pageNumber
        );
    }

    public TmdbDiscoverTv fetchTmdbTopRatedTv(int pageNumber) {
        return feignClient.getTopRatedTVs(
                apiProperties.getApiKey(),
                LANGUAGE_STRING,
                pageNumber
        );
    }

    public TmdbDiscoverMovie fetchTmdbTopRatedMovie(int pageNumber) {
        return feignClient.getTopRatedMovies(
                apiProperties.getApiKey(),
                LANGUAGE_STRING,
                pageNumber
        );
    }

    public TmdbDiscoverTv fetchTmdbTrendingTv(int pageNumber) {
        return feignClient.getTrendingTVs(
                apiProperties.getApiKey(),
                LANGUAGE_STRING,
                pageNumber
        );
    }

    public TmdbDiscoverMovie fetchTmdbTrendingMovie(int pageNumber) {
        return feignClient.getTrendingMovies(
                apiProperties.getApiKey(),
                LANGUAGE_STRING,
                pageNumber
        );
    }


    // append_to_response에 들어갈 값을 List<String>에서 추출함
    private String listStringToCommaSeparatedString(List<String> list) {
        return String.join(",", list);
    }

    // 장르 기반 작품 검색 시 장르 구분자로 사용
    private String listStringToVerticalBarSeparatedString(Set<Long> list) {
        StringJoiner joiner = new StringJoiner("|");
        for (Long genre : list) {
            joiner.add(String.valueOf(genre));
        }
        return joiner.toString();
    }
}
