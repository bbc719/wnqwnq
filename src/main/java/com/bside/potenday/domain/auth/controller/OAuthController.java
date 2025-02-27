package com.bside.potenday.domain.auth.controller;

import com.bside.potenday.domain.auth.domain.GoogleResponse;
import com.bside.potenday.domain.auth.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;
    // "https://accounts.google.com/o/oauth2/v2/auth?
    // client_id=56088644141-3f1vl6naah6coacgotdslat9d7fja917.apps.googleusercontent.com
    // &redirect_uri=http://localhost:8080/api/auth/callback
    // &response_type=code&scope=email%20profile
    // &access_type=offline"; -> Refresh token 발행 위함

//    @GetMapping("/google/login")
//    @Operation(summary = "oauth 인증 요청 url", description = "oauth 구글 인증 url 접속 메서드",
//            parameters = @Parameter(name = "requestUrl", description = "oauth 인증 요청 url"))
//    public ResponseEntity<Void> redirectToGoogle() {
//        String requestUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=56088644141-3f1vl6naah6coacgotdslat9d7fja917.apps.googleusercontent.com" +
//                "&redirect_uri=http://localhost:8080/api/auth/callback" +
//                "&response_type=code&scope=email%20profile" +
//                "&access_type=offline";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(requestUrl));
//        return new ResponseEntity<>(headers, HttpStatus.FOUND);
//    }

    @GetMapping("/callback")
    @Operation(summary = "oauth redirect url", description = "oauth 구글 인가 코드를 확인한다.")
    public ResponseEntity<GoogleResponse> successGoogleLogin(@RequestParam("code") String authCode) {
        return oAuthService.getGoogleAccessToken(authCode);
    }
}

