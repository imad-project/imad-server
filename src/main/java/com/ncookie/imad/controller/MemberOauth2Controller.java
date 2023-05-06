package com.ncookie.imad.controller;

import com.ncookie.imad.dto.request.TokenRequest;
import com.ncookie.imad.dto.response.SignInResponse;
import com.ncookie.imad.service.AuthService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberOauth2Controller {

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
    @PostMapping("/auth/token")
    public ResponseEntity<SignInResponse> refreshToken(@RequestBody TokenRequest tokenRequest){
        return ResponseEntity.ok(authService.refreshToken(tokenRequest));
    }
}
