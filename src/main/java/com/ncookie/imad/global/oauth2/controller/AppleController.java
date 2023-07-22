package com.ncookie.imad.global.oauth2.controller;

import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.oauth2.dto.AppleDTO;
import com.ncookie.imad.global.oauth2.dto.MsgEntity;
import com.ncookie.imad.global.oauth2.service.AppleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppleController {
    private final AppleService appleService;

    @PostMapping("/api/callback/apple")
    public ApiResponse<?> callback(HttpServletRequest request) {
        if (appleService.login(request.getParameter("code")) != null) {
            return ApiResponse.createSuccessWithNoContent(ResponseCode.LOGIN_SUCCESS);
        }

        return ApiResponse.createError(ResponseCode.LOGIN_FAILURE);
    }
}
