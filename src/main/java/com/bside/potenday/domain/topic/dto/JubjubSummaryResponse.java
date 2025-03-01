package com.bside.potenday.domain.topic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JubjubSummaryResponse {
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;
    @Schema(description = "오늘 완료한 줍줍 개수", example = "6")
    private long jubjubCount;
    @Schema(description = "오늘 완료한 총 줍줍 시간(분)", example = "60")
    private long totalJubjubTime;
}
