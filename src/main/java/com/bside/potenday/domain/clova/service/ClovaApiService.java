package com.bside.potenday.domain.clova.service;

import com.bside.potenday.domain.common.ApiResponse;
import com.bside.potenday.domain.feed.domain.Topic;
import com.bside.potenday.domain.feed.repository.TopicRepository;
import com.bside.potenday.domain.interest.domain.Interest;
import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.interest.dto.InterestDTO;
import com.bside.potenday.domain.interest.dto.UserInterestResponse;
import com.bside.potenday.domain.interest.repository.InterestsRepository;
import com.bside.potenday.domain.interest.repository.UserInterestsRepository;
import com.bside.potenday.domain.interest.service.InterestsService;
import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.timeSlot.repository.TimeSlotRepository;
import com.bside.potenday.domain.user.domain.User;
import com.bside.potenday.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.stream.Collectors;

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
    private UserRepository userRepository;
    @Autowired
    private UserInterestsRepository userInterestsRepository;
    @Autowired
    private InterestsRepository interestsRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private TopicRepository topicRepository;

    public UserInterestResponse getClovaResponse(Long userId) throws JsonProcessingException {
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(setPrompt(userId));

        // 요청 바디 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("topP", 0.8);
        requestBody.put("temperature", 0.5);
        requestBody.put("maxTokens", 500);
        requestBody.put("repeatPenalty", 1.0);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json"); // JSON 응답을 받도록 변경

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);
        Map<String, Object> responseBody = response.getBody();
        UserInterestResponse userInterestResponse = new UserInterestResponse();

        if (responseBody != null) {
            Map<String, Object> responseResult = (Map<String, Object>) responseBody.get("result");
            Map<String, Object> responseMessage = (Map<String, Object>) responseResult.get("message");
            String jsonResult = (String) responseMessage.get("content");

            ObjectMapper objectMapper = new ObjectMapper();
            userInterestResponse = objectMapper.readValue(jsonResult, UserInterestResponse.class);

            for (InterestDTO interest : userInterestResponse.getInterests()) {
                Long userInterestId = interest.getInterestId(); // 관심사 ID 가져오기

                for (String topicName : interest.getTopics()) {
                    // Topic 객체 생성 후 저장
                    Topic topic = new Topic(userId, userInterestId, topicName);
                    topicRepository.save(topic);
                }
            }
        }
        return userInterestResponse;
    }

    // 자투리 시간 계산
    private long calDuration(Long userId) {
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
        return minutes;
    }

    private Map<String, Object> setPrompt(Long userId) {
        int topicsPerInterest = 5;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        List<UserInterest> userInterests = userInterestsRepository.findByUserId(userId);
        List<Long> interestIds = userInterests.stream()
                .map(UserInterest::getInterestId)
                .collect(Collectors.toList());
        List<Interest> interests = interestsRepository.findByInterestIdIn(interestIds);
        Map<Long, String> interestMap = interests.stream()
                .collect(Collectors.toMap(Interest::getInterestId, Interest::getInterestName));

        List<String> interestNames = interestIds.stream()
                .map(interestMap::get)
                .filter(Objects::nonNull) // 혹시라도 null이 들어오는 경우 방지
                .collect(Collectors.toList());

        StringBuilder jsonFormat = new StringBuilder("[\n");
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");

        jsonFormat.append(String.format("  { \"userId\": %d, \"userName\": %s, " +
                                                "\"interests\": [",
                                                userId, user.getNickname()));
        for (Long interestId : interestIds) {
            jsonFormat.append(String.format("  { \"interestId\": %d, \"topics\": [", interestId));

            for (int i = 1; i <= topicsPerInterest; i++) {
                jsonFormat.append(String.format("\"주제%d\"%s", i, (i < topicsPerInterest) ? ", " : ""));
            }
            jsonFormat.append(" ] },\n");
        }
        jsonFormat.append(" ] },\n");

        if (jsonFormat.length() > 2) {
            jsonFormat.setLength(jsonFormat.length() - 2); // 마지막 쉼표 제거
        }
        jsonFormat.append("\n]");

        systemMessage.put("content", String.format(
                        "당신은 사용자의 관심사에 맞는 주제를 추천하는 AI입니다." +
                        " 사용자가 단어를 학습하고 싶은 관심사는 %s 입니다. 각 관심사에 대해 %d개의 주제를 추천하세요." +
                        " 추천하는 주제는 포괄적이고 실용적이어야 하며, " +
                        "예를 들면 다음과 같습니다: '입이 트이는 필수 회화 표현', '알아두면 좋은 생활 속 단어', '여행할 때 유용한 단어', '비즈니스에서 자주 쓰는 단어', '시험 대비 핵심 어휘'. " +
                        "출력 형식 (JSON):\\n%s\\n\\n",
                String.join(", ", interestNames),
                topicsPerInterest,
                jsonFormat.toString()

        ));

        return systemMessage;
    }
}