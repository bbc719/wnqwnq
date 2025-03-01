package com.bside.potenday.domain.topic.controller;

import com.bside.potenday.domain.common.ApiResult;
import com.bside.potenday.domain.topic.dto.JubjubSummaryResponse;
import com.bside.potenday.domain.topic.dto.TopicResponse;
import com.bside.potenday.domain.topic.service.TopicService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/topic")
@RequiredArgsConstructor
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/{userId}/topic")
    @Operation(summary = "저장된 주제 조회", description = "clova api 호출 전 아직 줍줍하지 않은 주제가 있다면 해당 주제를 보여준다. (5개가 안된다면 api호출하여 추가적으로 조회)")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 주제 가져옴",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TopicResponse.class)
            )
    )
    public TopicResponse getTopics(@PathVariable Long userId) throws JsonProcessingException {
        return topicService.getTopics(userId);
    }

    @PostMapping("/{userId}/jubjub")
    @Operation(summary = "주제 줍줍", description = "주제를 줍줍한다.")
    public ApiResult<Void> saveTopic(@PathVariable Long userId,
                                     @RequestParam Long topicId) {
        topicService.completeJubjub(userId, topicId);
        return ApiResult.successWithoutResponse(HttpStatus.OK.value());
    }

    @GetMapping("/{userId}/jubjub-count")
    @Operation(summary = "줍줍 개수", description = "사용자가 줍줍한 주제의 개수를 리턴한다.(미션완료)")
    public ApiResult<Long> getJubJub(@PathVariable Long userId) {
        return ApiResult.successResponse(HttpStatus.OK.value(), topicService.getJubjubCountByUser(userId));
    }

    @GetMapping("/{userId}/jubjub/summary")
    @Operation(summary = "오늘의 줍줍 요약 조회", description = "사용자가 오늘 완료한 줍줍 개수 및 총 줍줍 시간을 반환합니다.")
    public ApiResult<JubjubSummaryResponse> getTodayJubjubSummary(@PathVariable Long userId) {
        return ApiResult.successResponse(HttpStatus.OK.value(), topicService.getTodayJubjubSummary(userId));
    }
}
