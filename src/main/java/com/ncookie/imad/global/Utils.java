package com.ncookie.imad.global;

public class Utils {
    private static final String BEARER = "Bearer ";

    public static String extractToken(String token) {
        return token.replace(BEARER, "");
    }
}
