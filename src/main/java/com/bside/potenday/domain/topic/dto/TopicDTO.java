package com.bside.potenday.domain.topic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopicDTO {
    @Schema(description = "사용자의 관심사 ID", example = "1")
    private Long interestId;
    @Schema(description = "사용자의 관심사 이름", example = "영어 공부")
    private String interestName;
    @Schema(description = "추천 주제 목록", example = "[\"영어 회화 마스터하기\", \"비즈니스 영어 필수 표현\"]")
    private List<TopicDetailDTO> topics;
}