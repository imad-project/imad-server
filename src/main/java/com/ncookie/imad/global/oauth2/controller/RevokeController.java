package com.ncookie.imad.global.oauth2.controller;

import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.oauth2.service.RevokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class RevokeController {
    private final RevokeService revokeService;

    @DeleteMapping("/api/oauth2/revoke/apple")
    public ApiResponse<?> revokeAppleAccount(@RequestHeader("Authorization") String accessToken,
                                             @RequestParam(value = "ios") boolean isIOS) {
        revokeService.deleteAppleAccount(accessToken, isIOS);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.USER_DELETE_SUCCESS);
    }

    @DeleteMapping("/api/oauth2/revoke/google")
    public ApiResponse<?> revokeGoogleAccount(@RequestHeader("Authorization") String accessToken) {
        revokeService.deleteGoogleAccount(accessToken);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.USER_DELETE_SUCCESS);
    }

    @DeleteMapping("/api/oauth2/revoke/naver")
    public ApiResponse<?> revokeNaverAccount(@RequestHeader("Authorization") String accessToken) {
        revokeService.deleteNaverAccount(accessToken);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.USER_DELETE_SUCCESS);
    }

    @DeleteMapping("/api/oauth2/revoke/kakao")
    public ApiResponse<?> revokeKakaoAccount(@RequestHeader("Authorization") String accessToken) {
        revokeService.deleteKakaoAccount(accessToken);
        return ApiResponse.createSuccessWithNoContent(ResponseCode.USER_DELETE_SUCCESS);
    }
}
