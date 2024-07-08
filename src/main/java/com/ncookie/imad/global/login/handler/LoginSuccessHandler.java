package com.ncookie.imad.global.login.handler;

import com.ncookie.imad.domain.user.service.ProfileImageService;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
import com.ncookie.imad.global.Utils;
import com.ncookie.imad.global.jwt.property.JwtProperties;
import com.ncookie.imad.global.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@EnableConfigurationProperties({ JwtProperties.class })
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    private final UserRetrievalService userRetrievalService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출
        
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        // 로그인 시 response에 유저 정보 첨부
        UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

        // entity -> DTO 변환
        UserInfoResponse userInfoResponse = UserInfoResponse.toDTO(user);

        // 응답 헤더에 AccessToken, RefreshToken 실어서 응답
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(user.getEmail(), refreshToken);

        try {
            Utils.sendLoginSuccessResponseWithUserInfo(response, userInfoResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("로그인에 성공하였습니다.");
        log.info("이메일 : {}", email);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("로그인에 성공하였습니다. RefreshToken : {}", refreshToken);
        log.info("발급된 AccessToken 만료 기간 : {}", LocalDateTime.now().plusSeconds(jwtProperties.getAccess().getExpiration() / 1000));
        log.info("발급된 RefreshToken 만료 기간 : {}", LocalDateTime.now().plusSeconds(jwtProperties.getRefresh().getExpiration() / 1000));
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
