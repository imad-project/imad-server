package com.ncookie.imad.global.oauth2.controller;

import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.oauth2.service.AppleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppleController {
    private final AppleService appleService;

    @PostMapping("/api/callback/apple")
    public ApiResponse<?> callback(HttpServletRequest request, HttpServletResponse response) {
        // 애플 회원가입 또는 로그인 실패
        if (appleService.login(request.getParameter("code"), response) == null) {
            return ApiResponse.createError(ResponseCode.LOGIN_FAILURE);
        }

        return ApiResponse.createSuccessWithNoContent(ResponseCode.LOGIN_SUCCESS);
    }
}
