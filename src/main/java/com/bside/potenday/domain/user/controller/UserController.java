package com.bside.potenday.domain.user.controller;

import com.bside.potenday.domain.common.ApiResult;
import com.bside.potenday.domain.interest.service.InterestsService;
import com.bside.potenday.domain.user.domain.User;
import com.bside.potenday.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final InterestsService interestsService;

    @GetMapping("/{userId}/user")
    @Operation(summary = "사용자 조회", description = "사용자 정보 조회 메서드")
    public ApiResult<Optional<User>> getUser(@PathVariable Long userId) {
        return ApiResult.successResponse(HttpStatus.OK.value(), userService.findByUserId(userId));
    }

    @GetMapping("/{userId}/nickname")
    @Operation(summary = "회원가입 완료 여부 확인", description = "회원가입이 완료되었는지 확인 후 닉네임 저장 혹은 피드 api를 호출한다.")
    public ApiResult<Boolean> getIsCompleted(@PathVariable Long userId) {
        return ApiResult.successResponse(HttpStatus.OK.value(), userService.getIsCompleted(userId));
    }

    @PutMapping("/{userId}/nickname")
    @Operation(summary = "사용자 닉네임 저장", description = "사용자 닉네임 저장 메서드")
    public ApiResult<Void> updateUserProfile(@PathVariable Long userId, @RequestParam("nickname") String nickname) {
        userService.updateNickname(userId, nickname);
        return ApiResult.successWithoutResponse(HttpStatus.OK.value());
    }

    @PostMapping("/{userId}/interests")
    @Operation(
            summary = "관심사 저장",
            description = "사용자별 관심사를 등록한다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "관심사 정보 저장 테스트",
                    required = true
            )
    )
    public ApiResult<Void> saveUserInterest(@PathVariable Long userId, @RequestBody List<Long> interestIds) {
        interestsService.saveUserInterest(userId, interestIds);
        return ApiResult.successWithoutResponse(HttpStatus.OK.value());
    }
}
