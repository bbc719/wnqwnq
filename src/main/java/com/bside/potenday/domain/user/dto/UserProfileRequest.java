package com.bside.potenday.domain.user.dto;

import com.bside.potenday.domain.user.domain.Job;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserProfileRequest {
    @Schema(description = "사용자 고유 ID", example = "1")
    private Long userId;
    @Schema(description = "사용자 닉네임", example = "칠리새우먹고싶다")
    private String nickname;
    @Schema(description = "사용자 직업", example = "STUDENT")
    private Job job;
}
