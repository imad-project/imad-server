package com.ncookie.imad.domain.user.service;

import com.ncookie.imad.domain.user.entity.AuthProvider;
import com.ncookie.imad.domain.user.entity.Role;
import com.ncookie.imad.domain.user.dto.request.SignUpRequest;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserAccountService {
    public static final String SIGNUP_DUPLICATED_EMAIL = "SIGNUP_DUPLICATED_EMAIL";
    public static final String SIGNUP_DUPLICATED_NICKNAME = "SIGNUP_DUPLICATED_NICKNAME";

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;


    @Description("자체 로그인")
    public Long signUp(SignUpRequest signUpRequest) {
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

        return userAccountRepository.save(user).getId();
    }

    @Description("일반 회원 생성")
    @Transactional
    public String createUserAccount(SignUpRequest signUpRequest) {
//        if (userAccountRepository.existsByIdAndAuthProvider(signUpRequest.getId(), AuthProvider.IMAD)) {
//            throw new BadRequestException("이미 존재하는 유저입니다");
//        }

//        UserAccountDto dto = UserAccountDto.of(
//                signUpRequest.getId(),
//                signUpRequest.getNickname(),
//                signUpRequest.getPassword(),
//                signUpRequest.getGender(),
//                signUpRequest.getEmail(),
//                signUpRequest.getAgeRange(),
//                signUpRequest.getProfileImageUrl(),
//                Role.USER,
//                signUpRequest.getAuthProvider()
//        );
//
//        return userAccountRepository.save(dto.toEntity()).getUserId();

        return null;
    }

    @Description("Oauth 회원 생성")
    @Transactional
    public String createOauthUserAccount(String id, AuthProvider authProvider, String profileImage) {
//        if (userAccountRepository.existsByIdAndAuthProvider(id, authProvider)) {
//            throw new BadRequestException("이미 존재하는 유저입니다");
//        }

//        UserAccountDto dto = UserAccountDto.of(
//                id,
//                null,
//                null,
//                null,
//                null,
//                -1,
//                profileImage,
//                Role.USER,
//                authProvider
//        );
//
//        return userAccountRepository.save(dto.toEntity()).getUserId();
        return null;
    }

}
