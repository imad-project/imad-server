package com.ncookie.imad.domain.user.controller;

import com.ncookie.imad.domain.user.dto.request.ModifyUserPasswordRequest;
import com.ncookie.imad.domain.user.dto.request.SignUpRequest;
import com.ncookie.imad.domain.user.dto.request.UserInfoDuplicationRequest;
import com.ncookie.imad.domain.user.dto.request.UserUpdateRequest;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.dto.response.UserInfoDuplicationResponse;
import com.ncookie.imad.domain.user.dto.response.UserModifyProfileImageResponse;
import com.ncookie.imad.domain.user.service.ProfileImageService;
import com.ncookie.imad.domain.user.service.UserAccountService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final ProfileImageService profileImageService;

    @Description("회원가입")
    @PostMapping("/api/signup")
    public ApiResponse<?> createUserAccount(HttpServletResponse response,
                                            @RequestBody SignUpRequest signUpRequest) {
        String email = userAccountService.signUp(signUpRequest);
        UserAccountService.TokenTuple tokens = userAccountService.getTokenInSignupProcess(email);
        response.setHeader("Authorization", tokens.accessToken());
        response.setHeader("Authorization-refresh", tokens.refreshToken());

        return ApiResponse.createSuccessWithNoContent(ResponseCode.SIGNUP_SUCCESS);
    }

    @Description("회원정보 조회")
    @GetMapping("/api/user")
    public ApiResponse<UserInfoResponse> getUserAccountInfo(@RequestHeader("Authorization") String accessToken) {
        return ApiResponse.createSuccess(ResponseCode.USER_INFO_GET_SUCCESS, userAccountService.getUserInfo(accessToken));
    }

    @Description("회원정보 수정")
    @PatchMapping("/api/user")
    public ApiResponse<UserInfoResponse> updateUserAccountInfo(@RequestHeader("Authorization") String accessToken,
                                                               @RequestBody UserUpdateRequest userUpdateRequest) {
        return ApiResponse.createSuccess(
                ResponseCode.USER_INFO_UPDATE_SUCCESS,
                userAccountService.updateUserAccountInfo(accessToken, userUpdateRequest));
    }

    @Description("프로필 이미지 수정 - 커스텀")
    @PatchMapping("/api/user/profile_image/custom")
    public ApiResponse<UserModifyProfileImageResponse> modifyCustomProfileImage(@RequestHeader("Authorization") String accessToken,
                                                                                @RequestParam("image") MultipartFile image) {
        return ApiResponse.createSuccess(ResponseCode.USER_PROFILE_IMAGE_MODIFY_SUCCESS,
                profileImageService.modifyCustomProfileImage(accessToken, image));
    }

    @Description("프로필 이미지 수정 - 기본 이미지")
    @PatchMapping("/api/user/profile_image/default")
    public ApiResponse<UserModifyProfileImageResponse> modifyDefaultProfileImage(@RequestHeader("Authorization") String accessToken,
                                                    @RequestParam("image") String defaultImageName) {
        return ApiResponse.createSuccess(ResponseCode.USER_PROFILE_IMAGE_MODIFY_SUCCESS,
                profileImageService.modifyDefaultProfileImage(accessToken, defaultImageName));
    }

    @Description("회원탈퇴")
    @DeleteMapping("/api/user")
    public ApiResponse<?> deleteUserAccountInfo(@RequestHeader("Authorization") String accessToken) {
        userAccountService.deleteUserAccount(accessToken);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.USER_DELETE_SUCCESS);
    }

    @Description("비밀번호 수정")
    @PatchMapping("/api/user/password")
    public ApiResponse<?> modifyUserPassword(@RequestHeader("Authorization") String accessToken,
                                             @RequestBody ModifyUserPasswordRequest modifyUserPasswordRequest) {
        userAccountService.modifyUserPassword(accessToken, modifyUserPasswordRequest);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.USER_MODIFY_PASSWORD_SUCCESS);
    }

    private final RedisTemplate<String, String> redisTemplate;

    @Description("이메일 중복 검사")
    @PostMapping("/api/user/validation/email")
    public ApiResponse<UserInfoDuplicationResponse> validateUserAccountEmail(@RequestBody UserInfoDuplicationRequest email) {
        return ApiResponse.createSuccess(ResponseCode.USER_INFO_VALIDATION , userAccountService.checkUserEmailDuplicated(email));
    }

    @Description("닉네임 중복 검사")
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
