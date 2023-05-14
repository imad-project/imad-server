package com.ncookie.imad.domain.user.controller;

import com.ncookie.imad.domain.user.dto.request.SignUpRequest;
import com.ncookie.imad.domain.user.dto.response.SignUpResponse;
import com.ncookie.imad.domain.user.dto.response.UserInfoResponse;
import com.ncookie.imad.domain.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserAccountController {
    private final UserAccountService userAccountService;

    @PostMapping("/api/signup")
    public ResponseEntity<SignUpResponse> createUserAccount(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userAccountService.signUp(signUpRequest));
    }

    @GetMapping("/api/user")
    public ResponseEntity<UserInfoResponse> getUserAccountInfo(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.ok(userAccountService.getUserInfo(accessToken));
    }
}
