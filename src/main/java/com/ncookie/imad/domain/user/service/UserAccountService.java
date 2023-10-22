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
import jdk.jfr.Description;
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
@Description("회원 관련 서비스")
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    private final UserRetrievalService userRetrievalService;

    private final PasswordEncoder passwordEncoder;


    @Description("일반회원 회원가입")
    public void signUp(SignUpRequest signUpRequest) {
        if (userAccountRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            log.error("회원가입 실패 : 이미 존재하는 이메일");
            throw new BadRequestException(ResponseCode.SIGNUP_EMAIL_DUPLICATED);
        }

        UserAccount user = UserAccount.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .authProvider(signUpRequest.getAuthProvider())
                .role(Role.GUEST)
                .build();

        user.passwordEncode(passwordEncoder);
        UserAccount entity = userAccountRepository.save(user);

        log.info("회원가입 성공 : {}", entity.getId());
    }

    @Description("회원정보 조회")
    public UserInfoResponse getUserInfo(String accessToken) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        log.info("유저 정보 조회 완료");
        return UserInfoResponse.toDTO(user);
    }

    @Description("회원 정보 수정")
    public UserInfoResponse updateUserAccountInfo(String accessToken, UserUpdateRequest userUpdateRequest) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // 닉네임 중복 불가
        userAccountRepository.findByNickname(userUpdateRequest.getNickname())
                .ifPresent(foundUser -> {
                    if (!foundUser.equals(user)) {
                        log.error("유저 정보 수정 실패 : 이미 존재하는 닉네임");
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

        log.info("유저 정보 수정 완료 : {}", user.getId());
        return UserInfoResponse.toDTO(userAccountRepository.save(user));
    }

    @Description("회원탈퇴")
    public void deleteUserAccount(String accessToken) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);
        log.info("회원탈퇴 완료");

        userAccountRepository.delete(user);
    }

    @Description("비밀번호 수정")
    public void modifyUserPassword(String accessToken, ModifyUserPasswordRequest modifyUserPasswordRequest) {
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        if (passwordEncoder.matches(modifyUserPasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(modifyUserPasswordRequest.getNewPassword());
            user.passwordEncode(passwordEncoder);
            userAccountRepository.save(user);
        } else {
            log.error("비밀번호 변경 실패 : 기존 비밀번호 정보가 옳지 않음");
            throw new BadRequestException(ResponseCode.USER_MODIFY_PASSWORD_FAILURE);
        }
    }

    @Description("이메일 중복 검사")
    public UserInfoDuplicationResponse checkUserEmailDuplicated(UserInfoDuplicationRequest userInfoDuplicationRequest) {
        log.info("이메일 중복 검사 시행");
        return UserInfoDuplicationResponse.builder()
                .validation(!userAccountRepository.existsByEmail(userInfoDuplicationRequest.getInfo()))
                .build();
    }

    @Description("닉네임 중복 검사")
    public UserInfoDuplicationResponse checkUserNicknameDuplicated(UserInfoDuplicationRequest userInfoDuplicationRequest) {
        log.info("닉네임 중복 검사 시행");
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

        log.info("[테스트] 이메일 리스트 조회 성공");
        return emailList;
    }

    // 닉네임 리스트 조회
    public List<String> getUserNicknameList() {
        List<String> nicknameList = new ArrayList<>();
        userAccountRepository.findAll()
                .forEach(user -> nicknameList.add(user.getNickname()));

        log.info("[테스트] 닉네임 리스트 조회 성공");
        return nicknameList;
    }
}
