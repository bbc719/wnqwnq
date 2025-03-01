package com.bside.potenday.domain.word.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordDetailDTO {
    @Schema(description = "단어")
    private String word;
    @Schema(description = "단어뜻")
    private String meaning;
    @Schema(description = "품사")
    private String pos;
    @Schema(description = "예문")
    private String ex;
    @Schema(description = "예문뜻")
    private String tr;
}
