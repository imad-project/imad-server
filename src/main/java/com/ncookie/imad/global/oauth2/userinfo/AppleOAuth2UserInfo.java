package com.ncookie.imad.global.oauth2.userinfo;

import java.util.Map;

public class AppleOAuth2UserInfo extends OAuth2UserInfo {
    public AppleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        System.out.println(attributes.values().toString());
        return null;
    }

    @Override
    public String getNickname() {
        System.out.println(attributes.values().toString());
        return null;
    }

    @Override
    public String getImageUrl() {
        System.out.println(attributes.values().toString());
        return null;
    }
}
