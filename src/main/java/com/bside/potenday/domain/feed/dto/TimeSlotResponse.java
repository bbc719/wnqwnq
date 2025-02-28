package com.bside.potenday.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TimeSlotResponse {
    private String timeslotName;
    private String duration;
}