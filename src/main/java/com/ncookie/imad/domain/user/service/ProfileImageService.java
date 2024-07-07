package com.ncookie.imad.domain.user.service;

import com.ncookie.imad.global.aws.AwsS3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ProfileImageService {
    private final AwsS3Service awsS3Service;

    // 프로필 이미지 파일 URL 반환
    public String getProfileImageUrl(String fileName) {
        return awsS3Service.getFileUrl(fileName);
    }

    public void updateProfileImage() {
//        awsS3Service.
    }
}
