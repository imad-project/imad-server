package com.ncookie.imad.global.oauth2.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "apple")
public class AppleProperties {
    private String teamId;
    private String loginKey;
    private String clientId;
    private String iOSClientId;
    private String redirectUrl;
    private String keyPath;
}
