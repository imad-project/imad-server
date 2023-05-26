package com.ncookie.imad.domain.user.service;

import com.ncookie.imad.domain.user.dto.request.ModifyUserPasswordRequest;
import com.ncookie.imad.domain.user.dto.request.UserUpdateRequest;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.entity.Role;
import com.ncookie.imad.domain.user.dto.request.SignUpRequest;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.jwt.service.JwtService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;
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

    public UserInfoResponse getUserInfo(String accessToken) {
        UserInfoResponse userInfoResponse;

        // token에 데이터가 들어있는 것은 이미 filter에서 검증했기 떄문에 추가로 검사하지는 않음
        String email = jwtService.extractClaimFromJWT(JwtService.CLAIM_EMAIL, accessToken).get();

        Optional<UserAccount> user = userAccountRepository.findByEmail(email);
        if (user.isPresent()) {
            userInfoResponse = UserInfoResponse.builder()
                    .email(user.get().getEmail())
                    .nickname(user.get().getNickname())
                    .authProvider(user.get().getAuthProvider())
                    .gender(user.get().getGender())
                    .ageRange(user.get().getAgeRange())
                    .profileImage(user.get().getProfileImage())
                    .role(user.get().getRole())
                    .build();
        } else {
            throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
        }

        return userInfoResponse;
    }

    public UserInfoResponse updateUserAccountInfo(String accessToken, UserUpdateRequest userUpdateRequest) {
        Optional<String> email = jwtService.extractClaimFromJWT(JwtService.CLAIM_EMAIL, accessToken);

        // 닉네임 중복 불가
        userAccountRepository.findByNickname(userUpdateRequest.getNickname())
                .ifPresent(user -> {
                    if (!user.getEmail().equals(email.get())) {
                        throw new BadRequestException(ResponseCode.NICKNAME_DUPLICATED);
                    }
                });

        email.flatMap(userAccountRepository::findByEmail)
                .ifPresent(user -> {
            user.setNickname(userUpdateRequest.getNickname());
            user.setAgeRange(userUpdateRequest.getAgeRange());
            user.setGender(userUpdateRequest.getGender());
            user.setProfileImage(userUpdateRequest.getProfileImage());
            user.authorizeUser();
            userAccountRepository.save(user);
        });

        UserAccount userAccount = userAccountRepository.findByEmail(email.get()).get();
        return UserInfoResponse.builder()
                .email(userAccount.getEmail())
                .nickname(userAccount.getNickname())
                .authProvider(userAccount.getAuthProvider())
                .gender(userAccount.getGender())
                .ageRange(userUpdateRequest.getAgeRange())
                .profileImage(userAccount.getProfileImage())
                .role(userAccount.getRole())
                .build();
    }

    public void deleteUserAccount(String accessToken) {

        jwtService.extractClaimFromJWT(JwtService.CLAIM_EMAIL, accessToken)
                .ifPresentOrElse(email ->
                                userAccountRepository.findByEmail(email)
                                .ifPresentOrElse(userAccountRepository::delete,
                                        () -> {
                                            throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
                                        }),
                        () -> {
                            throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
                        }
                );
    }

    public void modifyUserPassword(String accessToken, ModifyUserPasswordRequest modifyUserPasswordRequest) {
        jwtService.extractClaimFromJWT(JwtService.CLAIM_EMAIL, accessToken)
                .ifPresentOrElse(email -> {
                    userAccountRepository.findByEmail(email)
                            .ifPresentOrElse(user -> {
                                if (passwordEncoder.matches(modifyUserPasswordRequest.getOldPassword(), user.getPassword())) {
                                    user.setPassword(modifyUserPasswordRequest.getNewPassword());
                                    user.passwordEncode(passwordEncoder);
                                    userAccountRepository.save(user);
                                } else {
                                    throw new BadRequestException(ResponseCode.USER_MODIFY_PASSWORD_FAILURE);
                                }
                            }, () -> { throw new BadRequestException(ResponseCode.USER_NOT_FOUND); });
                }, () -> { throw new BadRequestException(ResponseCode.USER_NOT_FOUND); });
    }

}
