package com.bside.potenday.domain.feed.controller;

import com.bside.potenday.domain.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeedController {

    @Autowired
    private final FeedService feedService;

    @GetMapping("/feed")
    @Operation(summary = "feed 화면 접속", description = "현재 날짜와 줍줍 추천 날짜를 비교하기 위해 줍줍 추천 날짜를 조회한다.")
    public LocalDate getRecAt(@RequestParam("userId") Long userId, @RequestParam("interestId") Long interestId) {
        return feedService.getRecAt(userId, interestId);
    }
}
