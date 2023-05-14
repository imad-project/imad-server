package com.ncookie.imad.domain.user.service;

import com.ncookie.imad.domain.user.dto.response.SignUpResponse;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.entity.Role;
import com.ncookie.imad.domain.user.dto.request.SignUpRequest;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.dto.ResponseStatus;
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
    public static final String SIGNUP_DUPLICATED_EMAIL = "이미 존재하는 이메일입니다.";
    public static final String SIGNUP_DUPLICATED_NICKNAME = "이미 존재하는 닉네임입니다.";

    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Description("일반 회원")
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (userAccountRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new BadRequestException(SIGNUP_DUPLICATED_EMAIL);
        }

        if (userAccountRepository.findByNickname(signUpRequest.getNickname()).isPresent()) {
            throw new BadRequestException(SIGNUP_DUPLICATED_NICKNAME);
        }

        UserAccount user = UserAccount.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .nickname(signUpRequest.getNickname())
                .authProvider(signUpRequest.getAuthProvider())
                .role(Role.USER)
                .build();

        user.passwordEncode(passwordEncoder);
        userAccountRepository.save(user);

        return SignUpResponse.builder()
                .code(ResponseStatus.SIGNUP_SUCCESS)
                .statusCode(ResponseStatus.SIGNUP_SUCCESS.getCode())
                .message("일반 회원가입 성공")
                .build();
    }

    public UserInfoResponse getUserInfo(String accessToken) {
        UserInfoResponse userInfoResponse;

        Optional<String> email = jwtService.extractClaimFromJWT(JwtService.CLAIM_EMAIL, accessToken);

        if (email.isEmpty()) {
            throw new BadRequestException("해당 계정을 찾을 수 없습니다.");
        } else {
            Optional<UserAccount> user = userAccountRepository.findByEmail(email.get());
            if (user.isPresent()) {
                userInfoResponse = UserInfoResponse.builder()
                        .email(user.get().getEmail())
                        .nickname(user.get().getNickname())
                        .authProvider(user.get().getAuthProvider())
                        .gender(user.get().getGender())
                        .ageRange(user.get().getAgeRange())
                        .build();
            } else {
                throw new BadRequestException("해당 계정을 찾을 수 없습니다.");
            }
        }

        return userInfoResponse;
    }

}
