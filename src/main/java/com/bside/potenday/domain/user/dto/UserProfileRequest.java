package com.bside.potenday.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserProfileRequest {
    @Schema(description = "구글에서 가입한 username", example = "여민진")
    private String username;
    @Schema(description = "사용자 고유 메일", example = "mjyeo1213@gmail.com")
    private String email;
}
