package com.ncookie.imad.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.entity.AuthProvider;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Utils {
    public static int PAGE_SIZE = 10;

    private static final String BEARER = "Bearer ";

    public static String extractToken(String token) {
        return token.replace(BEARER, "");
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        log.info("> X-FORWARDED-FOR : " + ip);

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info("> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info(">  WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info("> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info("> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
            log.info("> getRemoteAddr : "+ip);
        }
        log.info("> Result : IP Address : "+ip);

        return ip;
    }

    public static void sendSuccessReissueToken(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(mapper.writeValueAsString(
                ApiResponse.createSuccessWithNoContent(ResponseCode.TOKEN_REISSUE_SUCCESS)));
    }

    public static void sendLoginSuccessResponseWithUserInfo(HttpServletResponse response, UserInfoResponse userInfoResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(mapper.writeValueAsString(
                ApiResponse.createSuccess(ResponseCode.LOGIN_SUCCESS, userInfoResponse)));
    }

    public static void sendLoginSuccessResponse(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(mapper.writeValueAsString(
                ApiResponse.createSuccessWithNoContent(ResponseCode.LOGIN_SUCCESS)));
    }

    public static void sendErrorResponse(HttpServletResponse response, int status, ResponseCode responseCode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(status);

        response.getWriter().write(mapper.writeValueAsString(ApiResponse.createError(responseCode)));
    }

    public static void logWithOauthProvider(AuthProvider provider, String msg) {
        log.info("[" + provider.getAuthProvider() + "] " + msg);
    }

    public static PageRequest getPageRequest(int pageNumber, String sortString, int order) {
        Sort sort;
        PageRequest pageable;
        try {
            if (order == 0) {
                // 오름차순 (ascending)
                sort = Sort.by(sortString).ascending();
                pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
            } else if (order == 1) {
                // 내림차순 (descending)
                sort = Sort.by(sortString).descending();
                pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
            } else {
                pageable = PageRequest.of(pageNumber, PAGE_SIZE);
            }
        } catch (PropertyReferenceException e) {
            // sort string에 잘못된 값이 들어왔을 때 에러 발생
            throw new BadRequestException(ResponseCode.WRONG_SORT_STRING);
        }

        return pageable;
    }

    // createdDate 필드를 기준 내림차순으로 설정한 Pageable 객체를 얻는 메소드
    public static Pageable getDefaultPageable(int pageNumber) {
        Sort sort = Sort.by("createdDate").descending();
        return PageRequest.of(pageNumber, Utils.PAGE_SIZE, sort);
    }
}
