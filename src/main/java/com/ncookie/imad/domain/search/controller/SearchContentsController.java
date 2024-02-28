package com.ncookie.imad.domain.search.controller;

import com.ncookie.imad.domain.search.service.SearchContentsService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchContentsController {
    private final SearchContentsService searchContentsService;

    @GetMapping("/contents")
    public ApiResponse<?> searchContents() {
        searchContentsService.searchTvProgramData();
        return ApiResponse.createSuccessWithNoContent(ResponseCode.CONTENTS_SEARCH_SUCCESS);
    }
}
