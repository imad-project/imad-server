package com.ncookie.imad.global.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncookie.imad.global.oauth2.dto.AppleDTO;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AppleService {

    @Value("${apple.team-id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.login-key}")
    private String APPLE_LOGIN_KEY;

    @Value("${apple.client-id}")
    private String APPLE_CLIENT_ID;

    @Value("${apple.redirect-url}")
    private String APPLE_REDIRECT_URL;

    @Value("${apple.key-path}")
    private String APPLE_KEY_PATH;

    private final static String APPLE_AUTH_URL = "https://appleid.apple.com";

    public String getAppleLogin() {
        return APPLE_AUTH_URL + "/auth/authorize"
                + "?client_id=" + APPLE_CLIENT_ID
                + "&redirect_uri=" + APPLE_REDIRECT_URL
                + "&response_type=code%20id_token&scope=name%20email&response_mode=form_post";
    }

    public String generateAuthToken(String code) throws IOException {
        if (code == null) throw new IllegalArgumentException("Failed get authorization code");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", APPLE_CLIENT_ID);
        params.add("client_secret", createClientSecretKey());
        params.add("code", code);
        params.add("redirect_uri", APPLE_REDIRECT_URL);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    APPLE_AUTH_URL + "/auth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("Apple Auth Token Error");
        }
    }

    public AppleDTO getAppleInfo(String code) throws java.text.ParseException {
        String userId;
        String email;
        String accessToken;

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(generateAuthToken(code));

            accessToken = String.valueOf(jsonObj.get("access_token"));

            // ID TOKEN을 통해 회원 고유 식별자 받기
            SignedJWT signedJWT = SignedJWT.parse(String.valueOf(jsonObj.get("id_token")));
            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();

            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), JSONObject.class);

            userId = String.valueOf(payload.get("sub"));
            email = String.valueOf(payload.get("email"));

            return AppleDTO.builder()
                    .id(userId)
                    .token(accessToken)
                    .email(email).build();
        } catch (ParseException | JsonProcessingException e) {
            throw new RuntimeException("Failed to parse json data");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createClientSecretKey() throws IOException {
        // headerParams 적재
        Map<String, Object> headerParamsMap = new HashMap<>();
        headerParamsMap.put("kid", APPLE_LOGIN_KEY);
        headerParamsMap.put("alg", "ES256");

        // clientSecretKey 생성
        return Jwts
                .builder()
                .setHeaderParams(headerParamsMap)
                .setIssuer(APPLE_TEAM_ID)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30)) // 만료 시간 (30초)
                .setAudience(APPLE_AUTH_URL)
                .setSubject(APPLE_CLIENT_ID)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(APPLE_KEY_PATH);
        String privateKey = new String(resource.getInputStream().readAllBytes());

        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();

        return converter.getPrivateKey(object);
    }
}
