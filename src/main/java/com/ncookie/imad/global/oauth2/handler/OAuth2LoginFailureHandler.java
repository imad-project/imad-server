package com.ncookie.imad.global.oauth2.handler;

import com.ncookie.imad.global.Utils;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.oauth2.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static com.ncookie.imad.global.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Optional<String> cookieRedirectUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // 리액트에서 로그인 시도한 경우
        if (cookieRedirectUrl.isPresent()) {
            String redirectUrl = UriComponentsBuilder.fromUriString(cookieRedirectUrl.get())
                    .queryParam("error", exception.getLocalizedMessage())
                    .build().toUriString();

            log.info("리액트 서버의 소셜 로그인 실패 : redirect 수행");
            redirectStrategy.sendRedirect(request, response, redirectUrl);
        } else {
            // 로그인 실패 response 생성
            Utils.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ResponseCode.LOGIN_FAILURE);

            log.info("모바일 애플리케이션의 소셜 로그인 실패");
        }

        log.info("소셜 로그인에 실패했습니다. 에러 메시지 : {}", exception.getMessage());
    }
}
