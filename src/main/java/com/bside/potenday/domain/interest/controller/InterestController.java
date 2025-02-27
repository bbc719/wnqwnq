package com.bside.potenday.domain.interest.controller;

import com.bside.potenday.domain.interest.domain.Interest;
import com.bside.potenday.domain.interest.service.InterestsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InterestController {

    @Autowired
    private InterestsService userInterestsService;

    @GetMapping("/interests")
    @Operation(summary = "관심사 조회", description = "관심사 종류를 조회해온다.")
    public List<Interest> getInterests() {
        return userInterestsService.getInterests();
    }
}
