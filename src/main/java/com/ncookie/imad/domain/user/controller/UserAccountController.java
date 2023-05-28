package com.ncookie.imad.domain.user.controller;

import com.ncookie.imad.domain.user.dto.request.ModifyUserPasswordRequest;
import com.ncookie.imad.domain.user.dto.request.SignUpRequest;
import com.ncookie.imad.domain.user.dto.request.UserInfoDuplicationRequest;
import com.ncookie.imad.domain.user.dto.request.UserUpdateRequest;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.dto.response.UserInfoDuplicationResponse;
import com.ncookie.imad.domain.user.service.UserAccountService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserAccountController {
    private final UserAccountService userAccountService;


    @PostMapping("/api/signup")
    public ApiResponse<?> createUserAccount(@RequestBody SignUpRequest signUpRequest) {
        userAccountService.signUp(signUpRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.SIGNUP_SUCCESS);
    }

    @GetMapping("/api/user")
    public ApiResponse<UserInfoResponse> getUserAccountInfo(@RequestHeader("Authorization") String accessToken) {
        return ApiResponse.createSuccess(ResponseCode.USER_INFO_GET_SUCCESS, userAccountService.getUserInfo(accessToken));
    }

    @PatchMapping("/api/user")
    public ApiResponse<UserInfoResponse> updateUserAccountInfo(@RequestHeader("Authorization") String accessToken,
                                                               @RequestBody UserUpdateRequest userUpdateRequest) {
        return ApiResponse.createSuccess(
                ResponseCode.USER_INFO_UPDATE_SUCCESS,
                userAccountService.updateUserAccountInfo(accessToken, userUpdateRequest));
    }

    @DeleteMapping("/api/user")
    public ApiResponse<?> deleteUserAccountInfo(@RequestHeader("Authorization") String accessToken) {
        userAccountService.deleteUserAccount(accessToken);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.USER_DELETE_SUCCESS);
    }

    @PatchMapping("/api/user/password")
    public ApiResponse<?> modifyUserPassword(@RequestHeader("Authorization") String accessToken,
                                             @RequestBody ModifyUserPasswordRequest modifyUserPasswordRequest) {
        userAccountService.modifyUserPassword(accessToken, modifyUserPasswordRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.USER_MODIFY_PASSWORD_SUCCESS);
    }

    @PostMapping("/api/user/validation/email")
    public ApiResponse<UserInfoDuplicationResponse> validateUserAccountEmail(@RequestBody UserInfoDuplicationRequest email) {
        return ApiResponse.createSuccess(ResponseCode.USER_INFO_VALIDATION , userAccountService.checkUserEmailDuplicated(email));
    }

    @PostMapping("/api/user/validation/nickname")
    public ApiResponse<UserInfoDuplicationResponse> validateUserAccountNickname(@RequestBody UserInfoDuplicationRequest nickname) {
        return ApiResponse.createSuccess(ResponseCode.USER_INFO_VALIDATION , userAccountService.checkUserNicknameDuplicated(nickname));
    }


    /*
     * ==================================================================
     * 개발 중 테스트용
     * ==================================================================
     */
    @GetMapping("/api/test/email/list")
    public ApiResponse<List<String>> getNicknameList() {
        return ApiResponse.createSuccess(ResponseCode.USER_INFO_GET_SUCCESS, userAccountService.getUserEmailList());
    }

    @GetMapping("/api/test/nickname/list")
    public ApiResponse<List<String>> getEmailList() {
        return ApiResponse.createSuccess(ResponseCode.USER_INFO_GET_SUCCESS, userAccountService.getUserNicknameList());
    }
}
