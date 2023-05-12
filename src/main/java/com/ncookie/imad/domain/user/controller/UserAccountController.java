package com.ncookie.imad.domain.user.controller;

import com.ncookie.imad.global.dto.request.SignUpRequest;
import com.ncookie.imad.global.dto.request.TokenRequest;
import com.ncookie.imad.global.dto.response.SignInResponse;
import com.ncookie.imad.global.oauth2.service.AuthService;
import com.ncookie.imad.domain.user.service.UserAccountService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final AuthService authService;

    @GetMapping("/login/oauth2/code/{registrationId}")
    public ResponseEntity<SignInResponse> oauthRedirect(
            @PathVariable("registrationId") String registrationId,
            @RequestParam("code") String code,
            @RequestParam("state") String state) {
        return ResponseEntity.ok(
                authService.redirect(
                        TokenRequest.builder()
                                .registrationId(registrationId)
                                .code(code)
                                .state(state)
                                .build()));
    }

    @Description("JWT refresh token 발급")
    @PostMapping("/api/auth/token")
    public ResponseEntity<SignInResponse> refreshToken(@RequestBody TokenRequest tokenRequest){
        return ResponseEntity.ok(authService.refreshToken(tokenRequest));
    }

    @PostMapping("/api/signup")
    public ResponseEntity<String> createUserAccount(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userAccountService.createUserAccount(signUpRequest));
    }
}
