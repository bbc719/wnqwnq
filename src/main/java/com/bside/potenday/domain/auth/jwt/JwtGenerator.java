package com.bside.potenday.domain.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtGenerator {
    private final Key key;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;  // 1시간 (ms)
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;  // 7일 (ms)
    private static final String GRANT_TYPE = "Bearer";

    // application.yml에서 secret 값 가져와서 key에 저장
    public JwtGenerator(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JwtResponse generateToken(Long userId, List<String> roles) {
        // 권한 가져오기

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("auth", String.join(",", roles))
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtResponse.builder()
                .grantType(GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}