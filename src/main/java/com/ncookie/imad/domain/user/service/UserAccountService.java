package com.ncookie.imad.domain.user.service;

import com.ncookie.imad.domain.user.dto.request.ModifyUserPasswordRequest;
import com.ncookie.imad.domain.user.dto.request.UserInfoDuplicationRequest;
import com.ncookie.imad.domain.user.dto.request.UserUpdateRequest;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.dto.response.UserInfoDuplicationResponse;
import com.ncookie.imad.domain.user.entity.Role;
import com.ncookie.imad.domain.user.dto.request.SignUpRequest;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    private final UserRetrievalService userRetrievalService;

    private final PasswordEncoder passwordEncoder;


    // 일반회원 회원가입
    public void signUp(SignUpRequest signUpRequest) {
        if (userAccountRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new BadRequestException(ResponseCode.SIGNUP_EMAIL_DUPLICATED);
        }

        UserAccount user = UserAccount.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .authProvider(signUpRequest.getAuthProvider())
                .role(Role.GUEST)
                .build();

        user.passwordEncode(passwordEncoder);
        userAccountRepository.save(user);
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        return UserInfoResponse.toDTO(user);
    }

    public UserInfoResponse updateUserAccountInfo(String accessToken, UserUpdateRequest userUpdateRequest) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // 닉네임 중복 불가
        userAccountRepository.findByNickname(userUpdateRequest.getNickname())
                .ifPresent(foundUser -> {
                    if (!foundUser.equals(user)) {
                        throw new BadRequestException(ResponseCode.NICKNAME_DUPLICATED);
                    }
                });

        user.setNickname(userUpdateRequest.getNickname());
        user.setAgeRange(userUpdateRequest.getAgeRange());
        user.setGender(userUpdateRequest.getGender());
        user.setProfileImage(userUpdateRequest.getProfileImage());

        user.setPreferredTvGenres(userUpdateRequest.getPreferredTvGenres());
        user.setPreferredMovieGenres(userUpdateRequest.getPreferredMovieGenres());

        user.authorizeUser();

        return UserInfoResponse.toDTO(userAccountRepository.save(user));
    }

    public void deleteUserAccount(String accessToken) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        userAccountRepository.delete(user);
    }

    public void modifyUserPassword(String accessToken, ModifyUserPasswordRequest modifyUserPasswordRequest) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        if (passwordEncoder.matches(modifyUserPasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(modifyUserPasswordRequest.getNewPassword());
            user.passwordEncode(passwordEncoder);
            userAccountRepository.save(user);
        } else {
            throw new BadRequestException(ResponseCode.USER_MODIFY_PASSWORD_FAILURE);
        }
    }

    public UserInfoDuplicationResponse checkUserEmailDuplicated(UserInfoDuplicationRequest userInfoDuplicationRequest) {
        return UserInfoDuplicationResponse.builder()
                .validation(!userAccountRepository.existsByEmail(userInfoDuplicationRequest.getInfo()))
                .build();
    }

    public UserInfoDuplicationResponse checkUserNicknameDuplicated(UserInfoDuplicationRequest userInfoDuplicationRequest) {
        return UserInfoDuplicationResponse.builder()
                .validation(!userAccountRepository.existsByNickname(userInfoDuplicationRequest.getInfo()))
                .build();
    }

    /**
     * ==================================================================
     * 개발 중 테스트용
     * ==================================================================
     */

    // 이메일 리스트 조회
    public List<String> getUserEmailList() {
        List<String> emailList = new ArrayList<>();
        userAccountRepository.findAll()
                .forEach(user -> emailList.add(user.getEmail()));

        return emailList;
    }

    // 닉네임 리스트 조회
    public List<String> getUserNicknameList() {
        List<String> nicknameList = new ArrayList<>();
        userAccountRepository.findAll()
                .forEach(user -> nicknameList.add(user.getNickname()));

        return nicknameList;
    }
}
