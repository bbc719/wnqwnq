package com.bside.potenday.domain.auth.service;

import com.bside.potenday.domain.auth.domain.GoogleInfResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;

@Service
public class GoogleOAuthService {

    private static final String GOOGLE_PUBLIC_KEYS_URL = "https://www.googleapis.com/oauth2/v3/certs";

    public GoogleInfResponse verifyGoogleIdToken(String idToken) throws Exception {
        // 1. JWT 파싱
        JWSObject jwsObject = null;
        try {
            jwsObject = JWSObject.parse(idToken);
        } catch (Exception e) {
            return null;
        }


        // 2. 구글 공개 키 가져오기
        JWKSet jwkSet = JWKSet.load(new URL(GOOGLE_PUBLIC_KEYS_URL));
        JWK jwk = jwkSet.getKeyByKeyId(jwsObject.getHeader().getKeyID());

        if (jwk == null || !(jwk instanceof RSAKey)) {
            throw new RuntimeException("유효하지 않은 Google ID 토큰입니다.");
        }

        // 3. 서명 검증
        RSAPublicKey publicKey = ((RSAKey) jwk).toRSAPublicKey();
        if (!jwsObject.verify(new com.nimbusds.jose.crypto.RSASSAVerifier(publicKey))) {
            throw new RuntimeException("Google ID 토큰 검증 실패");
        }

        // 4. 사용자 정보 추출
        JsonNode payload = new ObjectMapper().readTree(jwsObject.getPayload().toString());
        return GoogleInfResponse.builder()
                .sub(payload.get("sub").asText())  // Google의 고유 사용자 ID
                .email(payload.get("email").asText())
                .name(payload.get("name").asText())
                .picture(payload.get("picture").asText())
                .build();
    }
}
