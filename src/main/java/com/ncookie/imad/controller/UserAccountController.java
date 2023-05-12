package com.ncookie.imad.controller;

import com.ncookie.imad.dto.request.SignUpRequest;
import com.ncookie.imad.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserAccountController {
    private final UserAccountService userAccountService;

    @PostMapping("/signup")
    public ResponseEntity<String> createUserAccount(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userAccountService.createUserAccount(signUpRequest));
    }
}
