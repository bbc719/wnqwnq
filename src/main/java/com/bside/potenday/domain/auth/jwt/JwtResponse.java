package com.bside.potenday.domain.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponse {
    private String grantType;   // "Bearer"
    private String accessToken; // 액세스 토큰
    private String refreshToken; // 리프레시 토큰
}
