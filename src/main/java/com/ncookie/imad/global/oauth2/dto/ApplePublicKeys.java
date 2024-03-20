package com.ncookie.imad.global.oauth2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApplePublicKeys {
    private List<ApplePublicKey> keys;
}
