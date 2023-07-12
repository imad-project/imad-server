package com.ncookie.imad.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.jwt.filter.JwtAuthenticationFilter;
import com.ncookie.imad.global.jwt.filter.JwtExceptionFilter;
import com.ncookie.imad.global.jwt.service.JwtService;
import com.ncookie.imad.global.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.ncookie.imad.global.login.handler.LoginFailureHandler;
import com.ncookie.imad.global.login.handler.LoginSuccessHandler;
import com.ncookie.imad.global.login.service.LoginService;
import com.ncookie.imad.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.ncookie.imad.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.ncookie.imad.global.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.io.PrintWriter;


/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    // 로그인을 위한 유저 repository
    private final UserAccountRepository userRepository;

    // JWT 관련
    private final JwtService jwtService;
    private final JwtExceptionFilter jwtExceptionFilter;

    // 자체 로그인 관련
    private final LoginService loginService;
    private final ObjectMapper objectMapper;

    // 소셜 로그인 관련
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        AuthenticationEntryPoint unauthorizedEntryPoint =
                (request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    String json = mapper.writeValueAsString(ApiResponse.createError(ResponseCode.UNAUTHORIZED_REQUEST));
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(json);
                    writer.flush();
                };

        AccessDeniedHandler accessDeniedHandler =
                (request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    String json = mapper.writeValueAsString(ApiResponse.createError(ResponseCode.INVALID_REQUEST));
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(json);
                    writer.flush();
                };

        http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()

                // 세션 정책 설정
                .sessionManagement()        
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                //== Unauthorized, Forbidden 처리 ==//
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(unauthorizedEntryPoint)
                .and()

                //== URL별 권한 관리 옵션 ==//
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/signup",
                        "/api/user/validation/**",
                        "/api/callback/**",
                        "/api/test/**",
                        "/oauth2/**",
                        "/auth/**",
                        "/actuator/logfile",     // 브라우저에서
                        "/h2-console/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()

                //== 소셜 로그인 설정 ==//
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
                .userInfoEndpoint().userService(customOAuth2UserService); // customUserService 설정

        // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
        // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        http.addFilterAfter(jwtAuthenticationProcessingFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), JwtAuthenticationFilter.class);

        return http.getOrBuild();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * AuthenticationManager 설정 후 등록
     * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
     * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     *
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
     */
    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationFilter(jwtService, userRepository);
    }
}
