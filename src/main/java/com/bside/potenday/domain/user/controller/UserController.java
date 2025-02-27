package com.bside.potenday.domain.user.controller;

import com.bside.potenday.domain.common.ApiResponse;
import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.interest.service.InterestsService;
import com.bside.potenday.domain.user.dto.UserProfileRequest;
import com.bside.potenday.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final InterestsService interestsService;

    @PutMapping("/user")
    @Operation(summary = "사용자 닉네임 저장", description = "사용자 닉네임 저장 메서드")
    public ApiResponse<Void> updateUserProfile(@RequestBody UserProfileRequest request) {
        userService.updateNickname(request);
        return ApiResponse.successWithoutResponse(HttpStatus.OK.value());
    }

    @PostMapping("/interests")
    @Operation(
            summary = "관심사 저장",
            description = "사용자별 관심사를 등록한다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "관심사 정보 저장 테스트",
                    required = true
            )
    )
    public ApiResponse<Void> saveUserInterest(@RequestBody List<UserInterest> userInterests) {
        interestsService.saveUserInterest(userInterests);
        return ApiResponse.successWithoutResponse(HttpStatus.OK.value());
    }
}
