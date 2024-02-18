package com.ncookie.imad.global.oauth2.controller;

import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.oauth2.service.AppleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class AppleController {
    private final AppleService appleService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @GetMapping("/login/apple")
    public void loginRequest(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "redirect_uri", required = false) String redirectUri) throws IOException {
        redirectStrategy.sendRedirect(request, response, appleService.getAppleLoginUrl(redirectUri));
    }

    @PostMapping("/api/callback/apple")
    public ApiResponse<?> callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUri = request.getParameter("state");

        UserAccount user = appleService.login(request.getParameter("code"));

        // 애플 회원가입 또는 로그인 실패
        if (user == null) {
            return ApiResponse.createError(ResponseCode.LOGIN_FAILURE);
        }

        // 리액트로 로그인한 경우
        if (redirectUri != null && !redirectUri.isEmpty()) {
            redirectStrategy.sendRedirect(request, response, appleService.determineRedirectUrl(user, redirectUri));
            return null;
        }

        // 네이티브 앱이나 기타 경로에서 로그인한 경우
        appleService.loginSuccess(user, response);
        return ApiResponse.createSuccess(ResponseCode.LOGIN_SUCCESS, UserInfoResponse.toDTO(user));
    }
}
