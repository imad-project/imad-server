package com.ncookie.imad.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.global.jwt.property.JwtProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
@EnableConfigurationProperties({ JwtProperties.class })
@Service
public class JwtService {
    private final JwtProperties jwtProperties;


    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
     * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_AUTH_PROVIDER = "authProvider";

    private static final String BEARER = "Bearer ";

    private final UserAccountRepository userRepository;

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT) // JWT 의 Subject 지정 -> AccessToken 이므로 AccessToken
                .withExpiresAt(new Date(now.getTime() + jwtProperties.getAccess().getExpiration())) // 토큰 만료 시간 설정

                // claim 으로 email 사용
                .withClaim(CLAIM_EMAIL, email)
                .sign(Algorithm.HMAC512(jwtProperties.getSecretKey())); // HMAC512 알고리즘 사용, application-jwt.yml 에서 지정한 secret 키로 암호화
    }

    /**
     * RefreshToken 생성
     * RefreshToken은 Claim 넣지 않으므로 withClaim() X
     */
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + jwtProperties.getRefresh().getExpiration()))
                .sign(Algorithm.HMAC512(jwtProperties.getSecretKey()));
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken){
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(jwtProperties.getAccess().getHeader(), accessToken);
        response.setHeader(jwtProperties.getRefresh().getHeader(), refreshToken);

        log.info("Headers : " + response.getHeaderNames().toString());
        log.info("AccessToken 설정 완료 : {}", accessToken);
        log.info("RefreshToken 설정 완료 : {}", refreshToken);
    }

    /**
     * 헤더에서 RefreshToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getRefresh().getHeader()))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getAccess().getHeader()))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 claim 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후 유효하다면 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractClaimFromJWT(String claim, String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(jwtProperties.getSecretKey()))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim(claim)
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    /**
     * RefreshToken DB 저장(업데이트)
     */
    public void updateRefreshToken(String email, String refreshToken) {
        Optional<UserAccount> optionalUserAccount = userRepository.findByEmail(email);

        if (optionalUserAccount.isPresent()) {
            UserAccount user = optionalUserAccount.get();

            user.updateRefreshToken(refreshToken);
            userRepository.saveAndFlush(user);
        } else {
            throw new BadRequestException(ResponseCode.REVIEW_WRONG_SORT_STRING);
        }
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(jwtProperties.getSecretKey())).build().verify(token);
            return true;
        } catch (TokenExpiredException e) {
            log.error("유효기간이 만료된 토큰입니다. {} {}", e.getMessage(), e.getExpiredOn());
            throw new TokenExpiredException(e.getMessage(), e.getExpiredOn());
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
}
