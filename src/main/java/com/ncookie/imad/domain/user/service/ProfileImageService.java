package com.ncookie.imad.domain.user.service;

import com.ncookie.imad.domain.user.dto.response.UserModifyProfileImageResponse;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.aws.AwsS3Service;
import com.ncookie.imad.global.aws.FileFolder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
public class ProfileImageService {
    private final UserRetrievalService userRetrievalService;
    private final UserAccountRepository userAccountRepository;

    private final AwsS3Service awsS3Service;

    // 프로필 이미지 파일 URL 반환
    public String getProfileImageUrl(String fileName) {
        return awsS3Service.getFileUrl(FileFolder.PROFILE_IMAGES.getValue() + fileName);
    }

    public UserModifyProfileImageResponse modifyCustomProfileImage(String accessToken, MultipartFile profileImage) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // 기존에 사용하던 프로필 이미지가 커스텀 이미지라면 S3에서 삭제
        String previousImage = user.getProfileImage();
        if (!previousImage.startsWith("default_profile_image_")) {
            awsS3Service.deleteFile(FileFolder.PROFILE_IMAGES, previousImage);
        }

        // 프로필 이미지 S3 업데이트
        String fileName = awsS3Service.uploadFile(profileImage, FileFolder.PROFILE_IMAGES);

        // DB 업데이트
        String fileNameWithoutDir = fileName.split("/")[1];     // 디렉터리 이름 중복 방지
        user.setProfileImage(fileNameWithoutDir);
        userAccountRepository.save(user);

        return UserModifyProfileImageResponse.of(getProfileImageUrl(fileNameWithoutDir));
    }

    public UserModifyProfileImageResponse modifyDefaultProfileImage(String accessToken, String defaultProfileImage) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // 기존에 사용하던 프로필 이미지가 커스텀 이미지라면 S3에서 삭제
        String previousImage = user.getProfileImage();
        if (!previousImage.startsWith("default_profile_image_")) {
            awsS3Service.deleteFile(FileFolder.PROFILE_IMAGES, previousImage);
        }
        
        // DB 업데이트
        user.setProfileImage(defaultProfileImage);
        userAccountRepository.save(user);

        return UserModifyProfileImageResponse.of(getProfileImageUrl(defaultProfileImage));
    }
}
