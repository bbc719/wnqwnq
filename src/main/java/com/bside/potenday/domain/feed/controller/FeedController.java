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

//    @GetMapping("/feed")
//    @Operation(summary = "feed 화면 접속", description = "현재 날짜와 줍줍 추천 날짜를 비교해 새로운 주제를 추천하기 위해 줍줍 추천 날짜를 조회한다.")
//    public LocalDateTime getRecAt(@RequestParam("userId") Long userId, @RequestParam("interestId") Long interestId) {
//        return feedService.getRecAt(userId, interestId);
//    }

//    1. 피드접속
//    2. 현재 시간을 확인 후 사용자가 설정한 자투리 시간에 속하는지 확인
//    3. 그 때의 자투리 시간(duration), 자투리이름을 return
//            1. 만약 현재 시간이 어느 자투리시간에도 속하지 않을 때 자투리 이름 = “자투리 시간”으로 return (duration은 빈값)

    @GetMapping("/feed")
    @Operation(summary = "feed 화면 접속(설정한 자투리 시간 확인)", description = "현재 시간을 확인해서 사용자의 어느 자투리 시간에 속하는지 확인한다.")
    public LocalDateTime getRecAt(@RequestParam("userId") Long userId, @RequestParam("interestId") Long interestId) {
        return feedService.getRecAt(userId, interestId);
    }
}
