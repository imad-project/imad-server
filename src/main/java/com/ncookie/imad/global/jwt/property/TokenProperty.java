package com.ncookie.imad.global.jwt.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenProperty {
    private Long expiration;
    private String header;
}
