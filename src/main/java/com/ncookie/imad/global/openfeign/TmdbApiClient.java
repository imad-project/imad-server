package com.ncookie.imad.global.openfeign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.contents.dto.ContentsSearchResponse;
import com.ncookie.imad.domain.tmdb.dto.TmdbDetails;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
    public ContentsSearchResponse searchByQuery(String query, String type, int page) {
        return switch (type) {
            case "multi" -> feignClient.searchMultiByQuery(apiProperties.getApiKey(), query, false, language, page);
            case "tv" -> feignClient.searchTvByQuery(apiProperties.getApiKey(), query, false, language, page);
            case "movie" -> feignClient.searchMovieByQuery(apiProperties.getApiKey(), query, false, language, page);
            default -> throw new BadRequestException(ResponseCode.CONTENTS_SEARCH_WRONG_TYPE);
        };
    }

    // 작품 상세 정보 조회
    public TmdbDetails getContentsDetails(int id, String type) {
        try {

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
        } catch (FeignException e) {
            throw new BadRequestException(ResponseCode.CONTENTS_NOT_EXIST_TMDB_ID);
        }
    }

    public String getContentsCertification(int id, String type) {
        ObjectMapper objectMapper = new ObjectMapper();
        String certification = "";
        
        // TV와 MOVIE의 시청등급을 얻어오는 방법이 다름
        try {
            if (type.equals("tv")) {
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

            } else if (type.equals("movie")) {
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


    // append_to_response에 들어갈 값을 List<String>에서 추출함
    private String listStringTocommaSeparatedString(List<String> list) {
        return String.join(",", list);
    }
}
