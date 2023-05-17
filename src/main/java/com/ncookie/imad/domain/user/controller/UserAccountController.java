package com.ncookie.imad.domain.user.controller;

import com.ncookie.imad.domain.user.dto.request.SignUpRequest;
import com.ncookie.imad.domain.user.dto.request.UserUpdateRequest;
import com.ncookie.imad.domain.user.dto.response.SignUpResponse;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.service.UserAccountService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserAccountController {
    private final UserAccountService userAccountService;

    @PostMapping("/api/signup")
    public ApiResponse<SignUpResponse> createUserAccount(@RequestBody SignUpRequest signUpRequest) {
        return ApiResponse.createSuccess(ResponseCode.SIGNUP_SUCCESS, userAccountService.signUp(signUpRequest));
    }

    @GetMapping("/api/user")
    public ApiResponse<UserInfoResponse> getUserAccountInfo(@RequestHeader("Authorization") String accessToken) {
        return ApiResponse.createSuccess(ResponseCode.GETTING_USER_INFO_SUCCESS, userAccountService.getUserInfo(accessToken));
    }

    @PatchMapping("/api/user")
    public ApiResponse<?> updateUserAccountInfo(@RequestBody UserUpdateRequest userUpdateRequest) {
//        return ApiResponse.createSuccess();
        return ApiResponse.createSuccessWithNoContent(ResponseCode.SIGNUP_SUCCESS);
    }
}
