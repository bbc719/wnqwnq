package com.bside.potenday.domain.clova.controller;

import com.bside.potenday.domain.topic.dto.TopicResponse;
import com.bside.potenday.domain.word.dto.WordResponse;
import com.bside.potenday.domain.clova.service.ClovaApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clova")
@RequiredArgsConstructor
public class ClovaApiController {

    @Autowired
    private final ClovaApiService clovaApiService;

//    @PostMapping("/{userId}/{interestId}/topic")
//    @Operation(summary = "clova api 호출(주제)", description = "사용자별 관심사에 관한 5가지의 주제를 추천 받는다.")
//    @ApiResponse(
//            responseCode = "200",
//            description = "성공적으로 주제를 추천받음",
//            content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = TopicResponse.class)
//            )
//    )
//    public TopicResponse getClovaTopicResponse(@PathVariable Long userId, @PathVariable Long interestId) throws JsonProcessingException {
//        return clovaApiService.getClovaTopicResponse(userId, interestId, "영어공부", 5);
//    }

    @PostMapping("/{userId}/{interestId}/word")
    @Operation(summary = "clova api 호출(단어)", description = "사용자별 관심사에 관한 단어를 추천 받는다.")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 단어를 추천받음",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WordResponse.class)
            )
    )
    public WordResponse getClovaWordResponse(@PathVariable Long userId, @PathVariable Long interestId, @RequestParam String title) throws JsonProcessingException {
        return clovaApiService.getClovaWordResponse(userId, interestId, title);
    }
}
