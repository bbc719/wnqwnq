package com.bside.potenday.domain.interest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InterestDTO {
    private Long interestId;
    private List<String> topics;
}
