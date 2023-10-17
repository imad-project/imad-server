package com.ncookie.imad.domain.user.service;

import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.global.jwt.service.JwtService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ncookie.imad.global.Utils.extractToken;


@Description("User entity 조회 용도로만 사용하는 서비스 클래스")
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserRetrievalService {
    private final JwtService jwtService;

    private final UserAccountRepository userAccountRepository;


    @Description("request에 포함된 JWT의 access token으로 UserAccount entity 조회")
    public UserAccount getUserFromAccessToken(String accessToken) {
        log.info("[Access Token 사용하여 UserAccount entity 조회]");

        log.info("액세스 토큰에서 이메일 정보를 추출");
        Optional<String> optionalEmail = jwtService.extractClaimFromJWT(JwtService.CLAIM_EMAIL, extractToken(accessToken));

        if (optionalEmail.isPresent()) {
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findByEmail(optionalEmail.get());
            if (optionalUserAccount.isEmpty()) {
                log.error("이메일 정보에 해당하는 유저를 찾을 수 없음");
                throw new BadRequestException(ResponseCode.USER_NOT_FOUND);
            } else {
                log.info("액세스 토큰에서 유저 entity를 얻는데 성공");
                return optionalUserAccount.get();
            }
        } else {
            log.warn("[이메일 정보 추출 실패] : JWT 토큰이 유효하지 않음");
            return null;
        }
    }
}
