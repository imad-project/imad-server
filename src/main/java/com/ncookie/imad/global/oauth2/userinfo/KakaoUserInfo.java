package com.ncookie.imad.global.oauth2.userinfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfo {
    private Long id;
    private KakaoAccount kakaoAccount;

    @Getter
    private static class KakaoAccount {
        private Profile profile;

        @Getter
        private static class Profile {
            private String nickname;
            private String profileImageUrl;
        }
    }
}