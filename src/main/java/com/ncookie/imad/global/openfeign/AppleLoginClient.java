package com.ncookie.imad.global.openfeign;

import com.ncookie.imad.global.oauth2.dto.ApplePublicKeys;
import com.ncookie.imad.global.oauth2.dto.AppleToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "appleClient", url = "https://appleid.apple.com/auth")
public interface AppleLoginClient {
    @GetMapping(value = "/keys")
    ApplePublicKeys getAppleAuthPublicKey();

    //아래 내용 추가
    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    AppleToken.Response getToken(AppleToken.Request request);
}
