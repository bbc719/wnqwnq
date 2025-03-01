package com.bside.potenday.domain.feed.controller;

import com.bside.potenday.domain.common.ApiResult;
import com.bside.potenday.domain.feed.dto.TimeSlotResponse;
import com.bside.potenday.domain.feed.service.FeedService;
import com.bside.potenday.domain.timeSlot.service.TimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {

    @Autowired
    private final FeedService feedService;
    @Autowired
    private final TimeSlotService timeSlotService;

    @GetMapping("/{userId}/timeSlot")
    @Operation(summary = "feed 화면 접속", description = "현재 시간을 확인해서 사용자의 어느 자투리 시간에 속하는지 확인한다.")
    public ApiResult<TimeSlotResponse> getCurrentTimeSlot(@PathVariable Long userId) {
        return ApiResult.successResponse(HttpStatus.OK.value(), feedService.getCurrentTimeSlot(userId));
    }
}
