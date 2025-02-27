package com.bside.potenday.domain.clova.controller;

import com.bside.potenday.domain.clova.service.ClovaApiService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClovaApiController {

    @Autowired
    private final ClovaApiService clovaApiService;

    @PostMapping("/clovar/topic")
    @Operation(summary = "clova api 호출(주제)", description = "사용자별 관심사에 관한 5가지의 주제를 추천 받는다.")
    public String getClovaResponse(@RequestParam("userId") Long userId) {
        return clovaApiService.getClovaResponse(userId);
    }
}
