package com.bside.potenday.domain.timeSlot.controller;

import com.bside.potenday.domain.common.ApiResult;
import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.timeSlot.dto.TimeSlotRequestDTO;
import com.bside.potenday.domain.timeSlot.service.TimeSlotService;
import com.bside.potenday.domain.topic.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timeSlot")
@RequiredArgsConstructor
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;
    @Autowired
    private TopicService topicService;

    @PostMapping("/{userId}/timeSlots")
    @Operation(summary = "사용자 자투리 시간 저장", description = "사용자의 자투리 시간을 저장한다. (09:00)")
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

    @GetMapping("/{userId}/totalMinutes")
    @Operation(summary = "사용자의 총 자투리 시간과 누적 줍줍 시간 조회",
            description = "사용자의 분 단위의 자투리 시간과 누적 줍줍 시간을 반환한다. (테스트 데이터 userId : 1, 자투리 180분 / 누적 50분")
    public ApiResult<Map<String, Integer>> getTotalTimeSlotMinutesAndJubjubTime(@PathVariable Long userId) {
        Map<String, Integer> result = timeSlotService.getTotalTimeSlotMinutesAndJubjubTime(userId);
        return ApiResult.successResponse(HttpStatus.OK.value(), result);
    }
}
