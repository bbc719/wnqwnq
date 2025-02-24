package com.bside.potenday.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

//    @PostMapping("/google/login")
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
}
