package com.ncookie.imad.global.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.entity.UserAccount;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserAccountRepository userRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private String refreshTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출
        
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        // 로그인 시 response에 유저 정보 첨부
        ObjectMapper mapper = new ObjectMapper();
        Optional<UserAccount> optionalUserAccount = userRepository.findByEmail(email);
        if (optionalUserAccount.isPresent()) {
            UserAccount user = optionalUserAccount.get();

            UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .authProvider(user.getAuthProvider())
                    .gender(user.getGender())
                    .ageRange(user.getAgeRange())
                    .profileImage(user.getProfileImage())
                    .role(user.getRole())
                    .build();

            try {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(mapper.writeValueAsString(
                        ApiResponse.createSuccess(ResponseCode.LOGIN_SUCCESS, userInfoResponse)));
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 응답 헤더에 AccessToken, RefreshToken 실어서 응답
            jwtService.updateRefreshToken(user.getEmail(), refreshToken);

            log.info("로그인에 성공하였습니다. 이메일 : {}", email);
            log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
            log.info("발급된 AccessToken 만료 기간 : {}", LocalDateTime.now().plusSeconds(Long.parseLong(accessTokenExpiration) / 1000));
            log.info("발급된 RefreshToken 만료 기간 : {}", LocalDateTime.now().plusSeconds(Long.parseLong(refreshTokenExpiration) / 1000));
        }
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
