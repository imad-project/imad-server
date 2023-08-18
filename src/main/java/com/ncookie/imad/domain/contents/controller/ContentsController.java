package com.ncookie.imad.domain.contents.controller;

import com.ncookie.imad.domain.contents.dto.SearchResponse;
import com.ncookie.imad.domain.contents.entity.ContentsType;
import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.domain.tmdb.dto.TmdbDetails;
import com.ncookie.imad.domain.tmdb.service.TmdbService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contents")
public class ContentsController {
    private final ContentsService contentsService;
    private final TmdbService tmdbService;

    @GetMapping("/search")
    public ApiResponse<SearchResponse> searchContentsByKeyword(@RequestParam(value = "query") String query,
                                                               @RequestParam(value = "type") String type,
                                                               @RequestParam(value = "page") int page) {
        return ApiResponse.createSuccess(
                ResponseCode.CONTENTS_SEARCH_SUCCESS,
                contentsService.searchKeywords(query, type, page)
        );
    }

    @GetMapping("/details")
    public ApiResponse<TmdbDetails> getContentsDetails(@RequestParam(value = "id") int id,
                                                       @RequestParam(value = "type") String type) {
        ContentsType contentsType = ContentsType.valueOf(type.toUpperCase());
        // 이전에 저장했던 데이터라면 TMDB API 사용하지 않고 로컬 DB 조회하여 반환
        if (contentsService.checkDuplicationExists(id, contentsType)) {
            return ApiResponse.createSuccess(
                    ResponseCode.CONTENTS_GET_DETAILS_SUCCESS,
                    tmdbService.getTmdbDetails(id, contentsType));
        } else {
            // TMDB API 사용하여 details 및 certification 정보 받아옴
            TmdbDetails contentsDetails = contentsService.getContentsDetails(id, type);
            String contentsCertification = contentsService.getContentsCertification(id, type);

            // 받아온 데이터를 DTO 클래스에 매핑하고, 데이터베이스에 저장함
            return ApiResponse.createSuccess(
                    ResponseCode.CONTENTS_GET_DETAILS_SUCCESS,
                    tmdbService.saveAndGetContentsDetails(contentsDetails, type, contentsCertification));
        }
    }
}
