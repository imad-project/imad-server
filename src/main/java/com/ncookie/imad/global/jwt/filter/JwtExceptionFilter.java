package com.ncookie.imad.global.jwt.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ncookie.imad.global.Utils;
import com.ncookie.imad.global.dto.response.ResponseCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            setErrorResponse(response);
        }
    }

    // 토큰 재발급 요청 시 첨부된 refresh token이 만료되었을 때 예외처리
    private void setErrorResponse(HttpServletResponse response) throws IOException {
        Utils.sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), ResponseCode.TOKEN_EXPIRED);
    }
}
