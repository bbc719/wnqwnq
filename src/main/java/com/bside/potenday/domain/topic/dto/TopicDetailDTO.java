package com.bside.potenday.domain.topic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopicDetailDTO {
    @Schema(description = "주제 ID", example = "10")
    private Long topicId;
    @Schema(description = "추천된 주제 이름", example = "영어 회화")
    private String topicName;
    @Schema(description = "줍줍 여부", example = "false")
    private Boolean jubjubYn;
}
