package com.bside.potenday.domain.auth.controller;

import com.bside.potenday.domain.auth.service.OAuthService;
import com.bside.potenday.domain.common.ApiResult;
import com.bside.potenday.domain.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;

    @PostMapping("/callback")
    @Operation(summary = "Google OAuth 로그인", description = "프론트엔드에서 받은 Authorization Code를 처리하여 사용자 정보를 반환합니다.")
    public ResponseEntity<ApiResult<User>> googleLogin(@RequestParam("code") String authCode) {
        return ResponseEntity.ok(ApiResult.successResponse(HttpStatus.OK.value(), oAuthService.getGoogleAccessToken(authCode)));
    }
}