package com.ncookie.imad.global.oauth2.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.global.dto.response.ResponseCode;
import com.ncookie.imad.global.exception.BadRequestException;
import com.ncookie.imad.global.oauth2.dto.ApplePublicKey;
import com.ncookie.imad.global.oauth2.dto.ApplePublicKeys;
import com.ncookie.imad.global.oauth2.property.AppleProperties;
import com.ncookie.imad.global.oauth2.openfeign.AppleLoginClient;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class AppleJwtUtils {
    private final AppleLoginClient appleLoginClient;
    private final AppleProperties appleProperties;

    public final static String APPLE_AUTH_URL = "https://appleid.apple.com";

    /**
     * User가 Sign in with Apple 요청(<a href="https://appleid.apple.com/auth/authorize">...</a>)으로 전달받은 id_token을 이용한 최초 검증
     * Apple Document URL ‣ <a href="https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_rest_api/verifying_a_user">...</a>
     *
     * @param idToken
     * @return boolean
     */
    public boolean verifyIdentityToken(String idToken, boolean isiOSApp) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            ReadOnlyJWTClaimsSet payload = signedJWT.getJWTClaimsSet();

            // EXP(만료기간) 검증
            Date currentTime = new Date(System.currentTimeMillis());
            if (!currentTime.before(payload.getExpirationTime())) {
                throw new BadRequestException(ResponseCode.OAUTH2_APPLE_ID_TOKEN_INVALID);
            }

            // ISS, AUD 검증
            String AUD = isiOSApp ? appleProperties.getIOSClientId() : appleProperties.getClientId();
            if (!APPLE_AUTH_URL.equals(payload.getIssuer()) || !AUD.equals(payload.getAudience().get(0))) {
                throw new BadRequestException(ResponseCode.OAUTH2_APPLE_ID_TOKEN_INVALID);
            }

            // RSA 검증
            if (verifyPublicKey(signedJWT)) {
                return true;
            }

        } catch (ParseException e) {
            throw new BadRequestException(ResponseCode.OAUTH2_APPLE_ID_TOKEN_INVALID);
        }

        return false;
    }

    /**
     * Apple Server에서 공개 키를 받아서 서명 확인
     *
     * @param signedJWT
     * @return
     */
    private boolean verifyPublicKey(SignedJWT signedJWT) {

        try {
            ApplePublicKeys keys = appleLoginClient.getAppleAuthPublicKey();
            ObjectMapper objectMapper = new ObjectMapper();

            for (ApplePublicKey key : keys.getKeys()) {
                RSAKey rsaKey = (RSAKey) JWK.parse(objectMapper.writeValueAsString(key));
                RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
                JWSVerifier verifier = new RSASSAVerifier(publicKey);

                if (signedJWT.verify(verifier)) {
                    return true;
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
