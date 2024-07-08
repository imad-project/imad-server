package com.ncookie.imad.domain.common;

import com.ncookie.imad.domain.user.dto.request.UserUpdateRequest;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.global.aws.AwsS3Service;
import com.ncookie.imad.global.aws.FileFolder;
import com.ncookie.imad.global.aws.S3Component;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Description("서비스의 퍼블릭 리소스에 접근하기 위해 사용하는 URL을 반환함")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resource")
public class ResourceAccessController {
    private final S3Component s3Component;

    @Description("프로필 이미지 조회 시 사용하는 URL(파일명 제외) 반환")
    @GetMapping("/profile")
    public ApiResponse<String> getProfileImageUrl() {
        return ApiResponse.createSuccess(ResponseCode.RESOURCE_URL_GET_SUCCESS,
                s3Component.getBucketUrl() + FileFolder.PROFILE_IMAGES.getValue());
    }

    @Description("게시글 이미지 조회 시 사용하는 URL(파일명 제외) 반환")
    @GetMapping("/posting")
    public ApiResponse<String> getPostingImageUrl() {
        return ApiResponse.createSuccess(ResponseCode.RESOURCE_URL_GET_SUCCESS,
                s3Component.getBucketUrl() + FileFolder.POSTING_IMAGES.getValue());
    }
}
