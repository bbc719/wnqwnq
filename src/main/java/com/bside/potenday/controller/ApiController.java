package com.bside.potenday.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {

    @GetMapping("/api/hello")
    @Operation(summary = "Hello API", description = "Spring Boot에서 간단한 Hello 메시지를 반환합니다.")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from Spring Boot!");
        return response;
    }
}

