package com.bside.potenday.domain.clova.service;

import com.bside.potenday.domain.interest.domain.Interest;
import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.interest.repository.InterestsRepository;
import com.bside.potenday.domain.interest.repository.UserInterestsRepository;
import com.bside.potenday.domain.interest.service.InterestsService;
import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.timeSlot.repository.TimeSlotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClovaApiService {
    @Value("${clova.api.url}")
    private String apiUrl;

    @Value("${clova.api.api-key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        // @Value 값이 주입된 후 초기화
        System.out.println("Clova API URL: " + apiUrl);
        System.out.println("Clova API Key: " + apiKey);
    }

    @Autowired
    private UserInterestsRepository userInterestsRepository;
    @Autowired
    private InterestsRepository interestsRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    public String getClovaResponse(Long userId) {


        // 사용자별 관심사를 조회한다.
        List<UserInterest> userInterests = userInterestsRepository.findByUserId(userId);
        List<String> languages = new ArrayList<>();
        for (UserInterest userInterest : userInterests) {
            languages.add(interestsRepository.findByInterestId(userInterest.getInterestId()).getInterestName());
        }

        // 사용자별 자투리시간을 조회한다.
        List<TimeSlotTemplate> slots = timeSlotRepository.findByUserId(userId);

        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        long minutes = 0L;

        for (TimeSlotTemplate slot : slots) {
            LocalTime start = slot.getStartTime();
            LocalTime end = slot.getEndTime();

            if (!now.isBefore(start) && !now.isAfter(end)) { // startTime ≤ now ≤ endTime
                minutes = Duration.between(start, end).toMinutes();
                break;
            }

        }

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + apiKey);
//        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", "19376c083d794a00bfe6f6b0cc580c99");
//        headers.set("Content-Type", "application/json");
//        headers.set("Accept", "text/event-stream");

        // messages 배열 생성
        List<Map<String, Object>> messages = new ArrayList<>();

        // System 역할 메시지
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", String.format(
                "자투리 시간을 활용하고 싶은 현대인들을 위한 자기개발 %s 단어장을 만들어줘.\n\n" +
                        "출력해야 되는 형식 (JSON)\n" +
                        "{\n" +
                        "  \"title\": \"\", \n" +
                        "  \"contents\": [\n" +
                        "    { \"word\": \"\", \"word_meaning\": \"\", \"pos\": \"\", \"ex_sentence\": \"\", \"ex_translate\": \"\" }\n" +
                        "  ],\n" +
                        "}\n\n" +
                        "주제는 5가지로 고정",
                String.join(", ", languages), minutes));

        messages.add(systemMessage);

        // 요청 바디 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("topP", 0.8);
        requestBody.put("temperature", 0.5);
        requestBody.put("maxTokens", 500);
        requestBody.put("repeatPenalty", 1.0);


        // JSON 변환
        try {
            String json = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 예외 로그 출력
        }


        // HTTP 요청 생성
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//        RestTemplate restTemplate1 = new RestTemplate();
//        ResponseEntity<Map> response = restTemplate1.postForEntity(apiUrl, requestEntity, Map.class);
//        Map<String, Object> responseBody = response.getBody();
//        Map<String, Object> responseResult = (Map<String, Object>) responseBody.get("result");
//        Map<String, Object> responseMessage = (Map<String, Object>) responseResult.get("message");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json"); // JSON 응답을 받도록 변경

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (responseBody != null) {
            Map<String, Object> responseResult = (Map<String, Object>) responseBody.get("result");
            Map<String, Object> responseMessage = (Map<String, Object>) responseResult.get("message");
            return (String) responseMessage.get("content");
        } else {
            throw new RuntimeException("응답이 비어 있습니다.");
        }

    }
}