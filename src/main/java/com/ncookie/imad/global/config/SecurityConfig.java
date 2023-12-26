package com.ncookie.imad.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.domain.user.repository.UserAccountRepository;
import com.ncookie.imad.domain.user.service.UserRetrievalService;
import com.ncookie.imad.global.dto.response.ApiResponse;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.jwt.filter.JwtAuthenticationFilter;
import com.ncookie.imad.global.jwt.filter.JwtExceptionFilter;
import com.ncookie.imad.global.jwt.property.JwtProperties;
import com.ncookie.imad.global.jwt.service.JwtService;
import com.ncookie.imad.global.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.ncookie.imad.global.login.handler.LoginFailureHandler;
import com.ncookie.imad.global.login.handler.LoginSuccessHandler;
import com.ncookie.imad.global.login.service.LoginService;
import com.ncookie.imad.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.ncookie.imad.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.ncookie.imad.global.oauth2.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.PrintWriter;

import static com.ncookie.imad.global.Utils.getClientIP;


/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@Configuration
@EnableConfigurationProperties({ JwtProperties.class })
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    // 로그인을 위한 유저 repository
    private final UserAccountRepository userRepository;
    private final UserRetrievalService userRetrievalService;

    // JWT 관련
    private final JwtService jwtService;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtProperties jwtProperties;

    // 자체 로그인 관련
    private final LoginService loginService;
    private final ObjectMapper objectMapper;

    // 소셜 로그인 관련
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Value("${ip.local.address}")
    private String myLocalIpAddress;


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
                .cors().configurationSource(corsConfigurationSource()).and()
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
                
                // 아래 URL로 들어오는 요청들은 Filter 검사에서 제외됨 
                .requestMatchers(
                        "/api/signup",
                        "/login/**",        // 소셜 로그인 redirect url
                        "/api/oauth2/**",
                        "/api/user/validation/**",
                        "/api/callback/**",
                        "/api/test/**",
                        "/oauth2/**",
                        "/aws",
                        "/auth/**",
                        "/h2-console/**")
                .permitAll()
                
                // 일부 GET 요청은 Filter 통과 (비회원도 자유롭게 조회 가능)
                .requestMatchers(HttpMethod.GET, "/api/contents/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/review/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posting/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/ranking/**").permitAll()

                // 모니터링 관련 데이터에 접근 가능한건 내 로컬 컴퓨터만
                .requestMatchers("/actuator/**").access(hasIpAddress(myLocalIpAddress))

                // 이 외 나머지 요청은 보안 처리
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 허용할 Origin 설정, *은 모든 Origin을 허용하는 것이므로 실제 환경에서는 제한 필요
        configuration.addAllowedMethod("*"); // 허용할 HTTP Method 설정
        configuration.addAllowedHeader("*"); // 허용할 HTTP Header 설정
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Authorization-refresh");
        configuration.setAllowCredentials(false); // Credentials를 사용할지 여부 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용

        return source;
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
        return new LoginSuccessHandler(jwtService, jwtProperties, userRetrievalService);
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

    private static AuthorizationManager<RequestAuthorizationContext> hasIpAddress(String ipAddress) {
        IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ipAddress);
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();
            return new AuthorizationDecision(ipAddressMatcher.matches(getClientIP(request)));
        };
    }
}
