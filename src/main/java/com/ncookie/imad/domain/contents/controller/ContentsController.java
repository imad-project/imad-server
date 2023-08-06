package com.ncookie.imad.domain.contents.controller;

import com.ncookie.imad.domain.contents.service.ContentsService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ContentsController {
    private final ContentsService contentsService;

    @GetMapping("/api/test/search")
    public ApiResponse<?> searchContentsByKeyword() {
        contentsService.searchKeywords();
        return ApiResponse.createSuccessWithNoContent(ResponseCode.SIGNUP_SUCCESS);
    }
}
