package com.ncookie.imad.domain.user.service;

import com.ncookie.imad.domain.user.dto.response.SignUpResponse;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.entity.Gender;
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

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Description("일반회원 회원가입")
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (userAccountRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new BadRequestException(ResponseCode.SIGNUP_EMAIL_DUPLICATED);
        }

        UserAccount user = UserAccount.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .nickname(null)
                .authProvider(signUpRequest.getAuthProvider())
                .ageRange(-1)
                .gender(Gender.NONE)
                .profileImage(-1)
                .role(Role.USER)
                .build();

        user.passwordEncode(passwordEncoder);
        Long userId = userAccountRepository.save(user).getId();

        return SignUpResponse.builder()
                .userId(userId)
                .build();
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        UserInfoResponse userInfoResponse;

        Optional<String> email = jwtService.extractClaimFromJWT(JwtService.CLAIM_EMAIL, accessToken);

        if (email.isEmpty()) {
            throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
        } else {
            Optional<UserAccount> user = userAccountRepository.findByEmail(email.get());
            if (user.isPresent()) {
                userInfoResponse = UserInfoResponse.builder()
                        .email(user.get().getEmail())
                        .nickname(user.get().getNickname())
                        .authProvider(user.get().getAuthProvider())
                        .gender(user.get().getGender())
                        .profileImage(user.get().getProfileImage())
                        .ageRange(user.get().getAgeRange())
                        .build();
            } else {
                throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
            }
        }

        return userInfoResponse;
    }

}
