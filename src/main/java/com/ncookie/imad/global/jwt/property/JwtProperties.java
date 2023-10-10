package com.ncookie.imad.global.jwt.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
// JWT 설정값을 불러오기 위한 propery 클래스
public class JwtProperties {
    private String secretKey;

    private TokenProperty access;
    private TokenProperty refresh;
}
