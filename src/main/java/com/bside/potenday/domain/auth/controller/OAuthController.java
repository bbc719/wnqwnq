package com.bside.potenday.domain.auth.controller;

import com.bside.potenday.domain.auth.service.OAuthService;
import com.bside.potenday.domain.common.ApiResponse;
import com.bside.potenday.domain.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;

    @GetMapping("/callback")
    @Operation(summary = "oauth redirect url", description = "oauth 구글 인가 코드를 확인한다.")
    public ApiResponse<User> successGoogleLogin(@RequestParam("code") String authCode) {
        return ApiResponse.successResponse(HttpStatus.OK.value(), oAuthService.getGoogleAccessToken(authCode));
    }
}

