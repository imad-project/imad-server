package com.ncookie.imad.global.oauth2.controller;

import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.oauth2.dto.AppleLoginResponse;
import com.ncookie.imad.global.oauth2.service.AppleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class AppleController {
    private final AppleService appleService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @GetMapping("/oauth2/login/apple")
    public void loginRequest(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "redirect_uri", required = false) String redirectUri) throws IOException {
        redirectStrategy.sendRedirect(request, response, appleService.getAppleLoginUrl(redirectUri));
    }

    @PostMapping("/api/callback/apple")
    public ApiResponse<?> callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AppleLoginResponse appleLoginResponse = AppleLoginResponse.builder()
                .state(request.getParameter("state"))
                .code(request.getParameter("code"))
                .idToken(request.getParameter("id_token"))
                .user(request.getParameter("user"))
                .build();

        String redirectUri = appleLoginResponse.getState();

        UserAccount user = appleService.loginWithRest(appleLoginResponse);
        boolean isValidRedirectUri = (redirectUri != null && !redirectUri.isEmpty());

        // 로그인 성공
        if (user != null) {
            // 리액트로 로그인한 경우
            if (isValidRedirectUri) {
                redirectStrategy.sendRedirect(request, response, appleService.determineSuccessRedirectUrl(user, redirectUri));
                return null;
            }

            // 네이티브 앱이나 기타 경로에서 로그인한 경우
            appleService.loginSuccess(user, response);
            return ApiResponse.createSuccess(ResponseCode.LOGIN_SUCCESS, UserInfoResponse.toDTO(user));
        }
        // 로그인 실패
        else {
            // 리액트로 로그인한 경우
            if (isValidRedirectUri) {
                redirectStrategy.sendRedirect(request, response, appleService.determineFailureRedirectUrl(redirectUri));
                return null;
            } else {
                return ApiResponse.createError(ResponseCode.LOGIN_FAILURE);
            }
        }
    }

    @PostMapping("/api/callback/apple/token")
    public ApiResponse<?> loginWithIdentityToken(HttpServletResponse response,
                                                 @RequestBody AppleLoginResponse appleLoginResponse) {
        UserAccount user = appleService.loginWithToken(appleLoginResponse);

        if (user != null) {
            appleService.loginSuccess(user, response);
            return ApiResponse.createSuccess(ResponseCode.LOGIN_SUCCESS, UserInfoResponse.toDTO(user));
        } else {
            return ApiResponse.createError(ResponseCode.LOGIN_FAILURE);
        }
    }
}
