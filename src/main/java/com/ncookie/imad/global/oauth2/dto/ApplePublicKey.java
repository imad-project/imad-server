package com.ncookie.imad.global.oauth2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplePublicKey {
    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String n;
    private String e;
}
