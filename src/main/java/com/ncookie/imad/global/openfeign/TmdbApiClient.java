package com.ncookie.imad.global.openfeign;

import com.ncookie.imad.domain.contents.dto.SearchResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// TMDB API를 사용하기 위한 메소드들을 모아놓은 클래스
@EnableConfigurationProperties({ TmdbApiProperties.class })
@RequiredArgsConstructor
@Component
public class TmdbApiClient {
    private final TmdbFeignClient feignClient;
    private final TmdbApiProperties apiProperties;

    private final String language = "ko-kr";
    private final List<String> appendResponseForDetails = new ArrayList<>(Arrays.asList("credits", "images", "videos"));


    // 쿼리로 작품 검색. 전체 / TV / 영화 별로 검색할 수 있음
    public SearchResponse searchByQuery(String query, String type, int page) {
        return switch (type) {
            case "multi" -> feignClient.searchMultiByQuery(apiProperties.getApiKey(), query, false, language, page);
            case "tv" -> feignClient.searchTvByQuery(apiProperties.getApiKey(), query, false, language, page);
            case "movie" -> feignClient.searchMovieByQuery(apiProperties.getApiKey(), query, false, language, page);
            default -> throw new BadRequestException(ResponseCode.CONTENTS_SEARCH_WRONG_TYPE);
        };
    }

    // 작품 상세 정보 조회
    public String getContentsDetails(int id, String type) {
        if (type.equals("tv")) {
            return feignClient.getTvDetailsById(
                    apiProperties.getApiKey(),
                    id,
                    language,
                    listStringTocommaSeparatedString(appendResponseForDetails));
        } else if (type.equals("movie")) {
            return feignClient.getMovieDetailsById(
                    apiProperties.getApiKey(),
                    id,
                    language,
                    listStringTocommaSeparatedString(appendResponseForDetails));
        } else {
            throw new BadRequestException(ResponseCode.CONTENTS_SEARCH_WRONG_TYPE);
        }
    }


    // append_to_response에 들어갈 값을 List<String>에서 추출함
    private String listStringTocommaSeparatedString(List<String> list) {
        return String.join(",", list);
    }
}
