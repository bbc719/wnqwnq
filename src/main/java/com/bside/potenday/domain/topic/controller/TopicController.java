package com.bside.potenday.domain.topic.controller;

import com.bside.potenday.domain.common.ApiResult;
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
    @Operation(summary = "사용자 학습 주제 조회", description = "사용자의 학습 주제를 가져오거나 부족하면 클로바 API를 호출하여 채운다.")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 학습 주제 가져옴",
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
        topicService.saveTopic(userId, topicId);
        return ApiResult.successWithoutResponse(HttpStatus.OK.value());
    }

    @GetMapping("/{userId}/jubjub-count")
    @Operation(summary = "줍줍 개수", description = "사용자가 줍줍한 주제의 개수를 리턴한다.(미션완료)")
    public ApiResult<Long> getJubJub(@PathVariable Long userId) {
        return ApiResult.successResponse(HttpStatus.OK.value(), topicService.getJubjubCountByUser(userId));
    }
}
