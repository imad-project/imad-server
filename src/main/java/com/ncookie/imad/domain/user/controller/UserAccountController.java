package com.ncookie.imad.domain.user.controller;

import com.ncookie.imad.domain.user.dto.request.SignUpRequest;
import com.ncookie.imad.domain.user.dto.response.SignUpResponse;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.service.UserAccountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserAccountController {
    private final UserAccountService userAccountService;

    @GetMapping("/")
    public String rootTest() {
        return "fuck";
    }

    @PostMapping("/api/signup")
    public ResponseEntity<SignUpResponse> createUserAccount(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userAccountService.signUp(signUpRequest));
    }

//    @GetMapping("/api/user")
//    public ResponseEntity<UserInfoResponse> getUserAccountInfo() {
//        return ResponseEntity.ok()
//    }

//    @GetMapping("/login/oauth2/code/{registrationId}")
//    public ResponseEntity<SignInResponse> oauthRedirect(
//            @PathVariable("registrationId") String registrationId,
//            @RequestParam("code") String code,
//            @RequestParam("state") String state) {
//        return ResponseEntity.ok(
//                authService.redirect(
//                        TokenRequest.builder()
//                                .registrationId(registrationId)
//                                .code(code)
//                                .state(state)
//                                .build()));
//    }
//
//    @Description("JWT refresh token 발급")
//    @PostMapping("/api/auth/token")
//    public ResponseEntity<SignInResponse> refreshToken(@RequestBody TokenRequest tokenRequest){
//        return ResponseEntity.ok(authService.refreshToken(tokenRequest));
//    }
}
