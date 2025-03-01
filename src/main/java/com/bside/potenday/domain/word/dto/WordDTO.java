package com.bside.potenday.domain.word.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WordDTO {
    @JsonProperty("interestId")
    @Schema(description = "사용자의 관심사 ID", example = "1")
    private Long interestId;
    @JsonProperty("wordList")
    @Schema(description = "추천 단어 목록")
    private List<WordDetailDTO> words;

    @JsonSetter("interestId")
    public void setInterestIdFromString(String interestId) {
        if (interestId == null || interestId.isBlank()) {
            System.err.println("⚠️ Warning: interestId is null or empty in JSON response");
            this.interestId = 0L; // 기본값 설정
        } else {
            this.interestId = Long.parseLong(interestId);
        }
    }
}