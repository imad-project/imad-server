package com.ncookie.imad.global.oauth2.handler;

import com.ncookie.imad.domain.user.service.ProfileImageService;
import jakarta.servlet.http.Cookie;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
import com.ncookie.imad.global.Utils;
import com.ncookie.imad.global.jwt.property.JwtProperties;
import com.ncookie.imad.global.jwt.service.JwtService;
import com.ncookie.imad.global.oauth2.CookieUtils;
import com.ncookie.imad.global.oauth2.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.ncookie.imad.global.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@EnableConfigurationProperties({ JwtProperties.class })
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRetrievalService userRetrievalService;
    private final ProfileImageService profileImageService;

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("OAuth2 Login 성공!");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);

        Optional<String> cookieRedirectUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // 리액트에서 로그인 시도한 경우
        if (cookieRedirectUrl.isPresent()) {
            String redirectUrl = UriComponentsBuilder.fromUriString(cookieRedirectUrl.get())
                    .path("/success")
                    .queryParam("token", accessToken)
                    .queryParam("refresh_token", refreshToken)
                    .build().toUriString();

            log.info("리액트 서버의 소셜 로그인 요청 : redirect 작업을 수행합니다.");
            redirectStrategy.sendRedirect(request, response, redirectUrl);
        } else {
            // 로그인 시 response에 유저 정보 첨부
            UserAccount user = userRetrievalService.getUserFromAccessToken(accessToken);

            // 프로필 이미지 URL 설정
            String profileImageUrl = profileImageService.getProfileImageUrl(user.getProfileImage());
            user.setProfileImage(profileImageUrl);

            UserInfoResponse userInfoResponse = UserInfoResponse.toDTO(user);

            log.info("모바일 애플리케이션의 소셜 로그인 요청 : 유저 정보를 첨부하여 응답합니다.");
            Utils.sendLoginSuccessResponseWithUserInfo(response, userInfoResponse);
        }

        log.info("로그인에 성공하였습니다.");
        log.info("이메일 : {}", oAuth2User.getEmail());
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("로그인에 성공하였습니다. RefreshToken : {}", refreshToken);
        log.info("발급된 AccessToken 만료 기간 : {}", LocalDateTime.now().plusSeconds(jwtProperties.getAccess().getExpiration() / 1000));
        log.info("발급된 RefreshToken 만료 기간 : {}", LocalDateTime.now().plusSeconds(jwtProperties.getRefresh().getExpiration() / 1000));
    }
    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
}
