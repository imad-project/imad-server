package com.ncookie.imad.service;

import com.ncookie.imad.domain.UserAccount;
import com.ncookie.imad.domain.type.AuthProvider;
import com.ncookie.imad.domain.type.Role;
import com.ncookie.imad.dto.UserAccountDto;
import com.ncookie.imad.dto.request.SignUpRequest;
import com.ncookie.imad.exception.BadRequestException;
import com.ncookie.imad.repository.UserAccountRepository;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    @Description("일반 회원 생성")
    @Transactional
    public String createUserAccount(SignUpRequest signUpRequest) {
        if (userAccountRepository.existsByUserIdAndAuthProvider(signUpRequest.getId(), AuthProvider.EMPTY)) {
            throw new BadRequestException("이미 존재하는 유저입니다");
        }

        UserAccountDto dto = UserAccountDto.of(
                signUpRequest.getId(),
                signUpRequest.getNickname(),
                signUpRequest.getPassword(),
                signUpRequest.getGender(),
                signUpRequest.getEmail(),
                signUpRequest.getAgeRange(),
                signUpRequest.getProfileImageUrl(),
                Role.USER,
                signUpRequest.getAuthProvider()
        );

        return userAccountRepository.save(dto.toEntity()).getUserId();
    }

    @Description("Oauth 회원 생성")
    @Transactional
    public String createOauthUserAccount(String id, AuthProvider authProvider, String profileImage) {
        if (userAccountRepository.existsByUserIdAndAuthProvider(id, authProvider)) {
            throw new BadRequestException("이미 존재하는 유저입니다");
        }

        UserAccountDto dto = UserAccountDto.of(
                id,
                null,
                null,
                null,
                null,
                -1,
                profileImage,
                Role.USER,
                authProvider
        );

        return userAccountRepository.save(dto.toEntity()).getUserId();
    }

}
