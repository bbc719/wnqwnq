package com.bside.potenday.domain.timeSlot.controller;

import com.bside.potenday.domain.common.ApiResult;
import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.timeSlot.dto.TimeSlotRequestDTO;
import com.bside.potenday.domain.timeSlot.service.TimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timeSlot")
@RequiredArgsConstructor
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;

    @PostMapping("/{userId}/timeSlots")
    public ApiResult<String> createOrUpdateTimeSlot(
            @PathVariable Long userId,
            @RequestBody List<TimeSlotRequestDTO> timeSlotRequests) {

        List<TimeSlotTemplate> timeSlots = timeSlotRequests.stream()
                .map(req -> {
                    if (req.getTemplateId() == null) {
                        return new TimeSlotTemplate(
                                userId,
                                req.getTemplateName(),
                                LocalTime.parse(req.getStartTime()),
                                LocalTime.parse(req.getEndTime())
                        );
                    } else {
                        return new TimeSlotTemplate(
                                req.getTemplateId(),
                                userId,
                                req.getTemplateName(),
                                LocalTime.parse(req.getStartTime()),
                                LocalTime.parse(req.getEndTime())
                        );
                    }
                })
                .collect(Collectors.toList());

        timeSlotService.createOrUpdateTimeSlot(userId, timeSlots);
        return ApiResult.successWithoutResponse(HttpStatus.OK.value());
    }
}
