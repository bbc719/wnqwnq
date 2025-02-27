package com.bside.potenday.domain.timeSlot.controller;

import com.bside.potenday.domain.common.ApiResponse;
import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.timeSlot.service.TimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;

    @PostMapping("/timeSlot")
    @Operation(
            summary = "자투리 시간 입력",
            description = "자투리 시간을 자유롭게 입력한다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "자투리 시간 정보 리스트",
                    required = true
            )
    )
    public ApiResponse<String> createTimeSlot(@RequestBody List<TimeSlotTemplate> timeSlot) {
        return ApiResponse.successWithoutResponse(HttpStatus.OK.value());
    }
}
