package com.ncookie.imad.domain.contents.controller;

import com.ncookie.imad.domain.contents.dto.SearchResponse;
import com.ncookie.imad.domain.contents.service.ContentsService;
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
    public ApiResponse<?> getContentsDetails(@RequestParam(value = "id") int id,
                                             @RequestParam(value = "type") String type) {
        contentsService.getContentsDetails(id, type);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.CONTENTS_GET_DETAILS_SUCCESS);
    }
}
