package com.ncookie.imad.global.oauth2.controller;

import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.oauth2.service.AppleService;
import com.ncookie.imad.global.oauth2.service.RevokeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AppleController {
    private final AppleService appleService;
    private final RevokeService revokeService;

    @PostMapping("/api/callback/apple")
    public ApiResponse<?> callback(HttpServletRequest request, HttpServletResponse response) {
        // 애플 회원가입 또는 로그인 실패
        if (appleService.login(request.getParameter("code"), response) == null) {
            return ApiResponse.createError(ResponseCode.LOGIN_FAILURE);
        }

        return ApiResponse.createSuccessWithNoContent(ResponseCode.LOGIN_SUCCESS);
    }

    @DeleteMapping("/api/oauth2/revoke/apple")
    public ApiResponse<?> revokeAppleAccount(@RequestHeader("Authorization") String accessToken) throws IOException {
        revokeService.deleteAppleAccount(accessToken);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.USER_DELETE_SUCCESS);
    }
}
