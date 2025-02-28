package com.bside.potenday.domain.interest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserInterestResponse {
    private Long userId;
    private String userName;
    private long duration;

    @JsonProperty("interest")
    private List<InterestDTO> interests;
}

