package com.bside.potenday.domain.topic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class TopicResponse {
    @Schema(description = "사용자 ID", example = "2")
    private Long userId;
    @Schema(description = "사용자 닉네임", example = "홍길동")
    private String nickname;
    @Schema(description = "자투리 시간 이름", example = "점심 시간")
    private String timeslotName;
    @JsonProperty("duration")
    @Schema(description = "사용 가능한 학습 시간 (분 단위)", example = "30")
    private String duration;
    @JsonProperty("topics")
    @Schema(description = "추천된 주제 목록")
    private List<TopicDTO> topics;
}