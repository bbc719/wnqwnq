package com.bside.potenday.domain.interest.dto;

import com.bside.potenday.domain.topic.dto.TopicDTO;
import com.bside.potenday.domain.word.dto.WordDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInterestResponse {
    private Long userId;
    private String userName;
    private String templateName;
    private long duration;

    @JsonProperty("topics")
    private List<TopicDTO> topicsDto;

    @JsonProperty("words")
    private List<WordDTO> wordsDto;
}