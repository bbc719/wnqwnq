package com.bside.potenday.domain.user.controller;

import com.bside.potenday.domain.user.dto.UserProfileRequest;
import com.bside.potenday.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/save")
    @Operation(summary = "사용자 닉네임,직업 저장", description = "사용자 닉네임 및 직업 저장 메서드")
    public ResponseEntity<String> updateNickname(@RequestBody UserProfileRequest request) {
        userService.updateNickname(request.getUserId(), request.getNickname(), request.getJob());
        return ResponseEntity.ok("닉네임 저장 완료");
    }
}
