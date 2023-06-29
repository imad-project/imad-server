package com.ncookie.imad.global.oauth2.controller;

import com.ncookie.imad.global.oauth2.dto.AppleDTO;
import com.ncookie.imad.global.oauth2.dto.MsgEntity;
import com.ncookie.imad.global.oauth2.service.AppleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppleController {
    private final AppleService appleService;

    @PostMapping("/api/callback/apple")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) throws Exception {
        AppleDTO appleInfo = appleService.getAppleInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body(new MsgEntity("Success", appleInfo));
    }
}
