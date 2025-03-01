package com.bside.potenday.domain.timeSlot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimeSlotRequestDTO {
    private Long templateId;
    private String templateName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String endTime;
}
