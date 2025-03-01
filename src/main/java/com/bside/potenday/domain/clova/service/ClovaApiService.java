package com.bside.potenday.domain.clova.service;

import com.bside.potenday.domain.topic.domain.Topic;
import com.bside.potenday.domain.topic.dto.TopicDTO;
import com.bside.potenday.domain.topic.dto.TopicDetailDTO;
import com.bside.potenday.domain.topic.dto.TopicResponse;
import com.bside.potenday.domain.word.domain.Word;
import com.bside.potenday.domain.feed.dto.TimeSlotResponse;
import com.bside.potenday.domain.topic.repository.TopicRepository;
import com.bside.potenday.domain.word.repository.WordRepository;
import com.bside.potenday.domain.interest.domain.Interest;
import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.interest.repository.InterestsRepository;
import com.bside.potenday.domain.interest.repository.UserInterestsRepository;
import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.timeSlot.repository.TimeSlotRepository;
import com.bside.potenday.domain.user.domain.User;
import com.bside.potenday.domain.user.repository.UserRepository;
import com.bside.potenday.domain.word.dto.WordDTO;
import com.bside.potenday.domain.word.dto.WordDetailDTO;
import com.bside.potenday.domain.word.dto.WordResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClovaApiService {
    @Value("${clova.api.url}")
    private String apiUrl;

    @Value("${clova.api.api-key}")
    private String apiKey;

    private static final int MAX_RETRIES = 5;

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
    @Autowired
    private WordRepository wordRepository;

    @PostConstruct
    private void init() {
        // @Value 값이 주입된 후 초기화
        System.out.println("Clova API URL: " + apiUrl);
        System.out.println("Clova API Key: " + apiKey);
    }

    public TopicResponse getClovaTopicResponse(Long userId, Long interestId, String interestName, int needCount) throws JsonProcessingException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        TimeSlotResponse timeSlotResponse = getCurrentTimeSlot(userId);
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(setPromptForTopics(userId, interestId, interestName, needCount));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(createRequestBody(messages), getHeaders());
        ResponseEntity<Map> response = new RestTemplate().postForEntity(apiUrl, requestEntity, Map.class);

        return processTopicResponse(userId, user.getNickname(),
                timeSlotResponse.getTimeslotName(), timeSlotResponse.getDuration(), response);
    }

    public WordResponse getClovaWordResponse(Long userId, Long interestId, String type) throws JsonProcessingException {
        TimeSlotResponse timeSlotResponse = getCurrentTimeSlot(userId);
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(setPromptForWords(userId, timeSlotResponse.getDuration(), interestId, type));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(createRequestBody(messages), getHeaders());
        ResponseEntity<Map> response = new RestTemplate().postForEntity(apiUrl, requestEntity, Map.class);

        return processWordResponse(userId, timeSlotResponse.getTimeslotName(), timeSlotResponse.getDuration(), response);
    }

    private Map<String, Object> setPromptForTopics(Long userId, Long interestId, String interestName, int needCount) {
        //int topicsPerInterest = needCount;
        List<UserInterest> userInterests = userInterestsRepository.findByUserId(userId);
        List<Interest> userInterestsList = interestsRepository.findByInterestIdIn(
                userInterests.stream().map(UserInterest::getInterestId).collect(Collectors.toList()));
        List<Long> interestIds = userInterestsList.stream()
                .map(Interest::getInterestId)
                .toList();
        List<String> interestNames = userInterestsList.stream()
                .map(Interest::getInterestName)
                .toList();

        StringBuilder jsonFormat = new StringBuilder("{ \"result\": [\n");
        jsonFormat.append(String.format("  { \"interestId\": \"%s\", \"interestName\": \"%s\", \"topics\": [",
                interestId, interestName));
        jsonFormat.append(" \"주제1\", \"주제2\", \"주제3\", \"주제4\", \"주제5\" ] }");

        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", String.format(
                "너는 20대 후반에서 30대 중반의 직장인의 %s 단어 공부를 도와주는 AI 비서다. 너는 공부할만한 표현의 주제를 추천해준다." +
                        "규칙1: 28세~36세 사이의 사무직 직장인에게 적절한 주제를 추천해준다." +
                        " 규칙2: 각 관심사에 맞는 언어의 주제를 한글로 추천해준다. (예: 직장인이 꼭 알아야 할 비즈니스 중국어)" +
                        " 규칙3: 주제는 20자를 넘지 않도록 하되 포괄적이고 자연스러운 표현으로 간략히 작성한다." +
                        " 주제는 %d개씩 추천해준다." +
                        "출력 형식 (JSON):\\n%s\\n\\n",
                interestName, needCount, jsonFormat
        ));
        return systemMessage;
    }

    private Map<String, Object> setPromptForWords(Long userId, String duration, Long interestId, String type) {
//        int totalWords = (duration.isBlank() || Integer.parseInt(duration) <= 10)
//                ? 5
//                : Math.min(Integer.parseInt(duration) / 2, 30);
        int totalWords = 10;

        StringBuilder jsonFormat = new StringBuilder("{ \"contents\": [\n");

        jsonFormat.append(String.format("  { \"interestId\": \"%s\", \"wordList\": [\n", interestId));

        for (int j = 1; j <= totalWords; j++) {
            jsonFormat.append("    { ");
            jsonFormat.append(String.format("\"word\": \"word%d\", ", j));
            jsonFormat.append(String.format("\"meaning\": \"meaning%d\", ", j));
            jsonFormat.append("\"pos\": \"adj\", ");
            jsonFormat.append(String.format("\"ex\": \"ex-%d\", ", j));
            jsonFormat.append(String.format("\"tr\": \"tr-%d\"", j));
            jsonFormat.append(" }");
        }
        jsonFormat.append("\n  ] }\n]}");

        // 클로바에 보낼 시스템 메시지 생성
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", String.format(
                "너는 28세~36세 사이인 직장인의 관심사인 " + type + "에 대한 단어를 추천해주는 AI 비서다. "
                        + "해당 관심사에 대한 단어(word)와 단어의 meaning, pos, example, translation을 함께 추천해준다. " +
                        " meaning은 word의 한국어 뜻, example은 해당 단어를 활용한 예시 문장, translation은 example의 한국어 해석이다.\n"
                        + "pos는 형용사, 부사, 명사, 동사로 한정한다. 각 표기는 adj., n., v., adv.로 표기한다.\n"
                        + "출력 형식 (JSON): \n%s\n\n", jsonFormat.toString()));

        return systemMessage;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        return headers;
    }

    private Map<String, Object> createRequestBody(List<Map<String, Object>> messages) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("topP", 0.6);
        requestBody.put("temperature", 0.3);
        requestBody.put("maxTokens", 2000);
        requestBody.put("repeatPenalty", 1.0);
        return requestBody;
    }

    private TopicResponse processTopicResponse(Long userId, String nickname, String timeslotName,
                                               String duration, ResponseEntity<Map> response) throws JsonProcessingException {
        TopicResponse topicResponse = new TopicResponse(userId, nickname, timeslotName, duration, new ArrayList<>());
        Map<String, Object> responseBody = response.getBody();
        System.out.println("Clova API 응답 데이터: " + responseBody);

        if (responseBody != null) {
            Map<String, Object> responseResult = (Map<String, Object>) responseBody.get("result");
            Map<String, Object> responseMessage = (Map<String, Object>) responseResult.get("message");
            String jsonResult = (String) responseMessage.get("content");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResult);
            JsonNode resultNode = rootNode.get("result");

            List<TopicDTO> topicDTOList = new ArrayList<>();
            List<Topic> topicsToSave = new ArrayList<>();

            for (JsonNode topicNode : resultNode) {
                Long interestId = topicNode.get("interestId").asLong();
                String interestName = topicNode.get("interestName").asText();
                JsonNode topicsArray = topicNode.get("topics");

                List<TopicDetailDTO> topicDetails = new ArrayList<>();
                for (JsonNode topic : topicsArray) {
                    Topic topicEntity = new Topic(userId, interestId, topic.asText());
                    topicsToSave.add(topicEntity);
                }

                topicDTOList.add(new TopicDTO(interestId, interestName, topicDetails));
            }

            List<Topic> savedTopics = topicRepository.saveAll(topicsToSave);

            int index = 0;
            for (TopicDTO topicDTO : topicDTOList) {
                for (int j = 0; j < 5; j++) { // 5개의 주제 저장
                    if (index < savedTopics.size()) {
                        Topic savedTopic = savedTopics.get(index++);
                        topicDTO.getTopics().add(new TopicDetailDTO(
                                savedTopic.getTopicId(),
                                savedTopic.getTopicName(),
                                savedTopic.getJubjubYn()
                        ));
                    }
                }
            }

            topicResponse.setTopics(topicDTOList);
        }
        return topicResponse;
    }

//    private WordResponse processWordResponse(Long userId, Long interestId, String timeSlotName,
//                                             String duration, ResponseEntity<Map> response) throws JsonProcessingException {
//        WordResponse wordResponse = new WordResponse(userId, timeSlotName, duration, new ArrayList<>());
//        Map<String, Object> responseBody = response.getBody();
//
//        if (responseBody != null) {
//            Map<String, Object> responseResult = (Map<String, Object>) responseBody.get("result");
//            Map<String, Object> responseMessage = (Map<String, Object>) responseResult.get("message");
//            String jsonResult = (String) responseMessage.get("content");
//            jsonResult = cleanJsonString(jsonResult);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(jsonResult);
//            try {
//                rootNode = objectMapper.readTree(jsonResult);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException("JSON 변환 실패: 응답 데이터가 올바른 JSON 형식이 아닙니다.\n응답 데이터: " + jsonResult, e);
//            }
//
//            List<WordDTO> wordDTOList = new ArrayList<>();
//            for (JsonNode node : rootNode) {
//                WordDTO wordDTO = new WordDTO();
//                wordDTO.setInterestId(interestId);
//
//                JsonNode wordListNode = node.get("wordList");
//                if (wordListNode != null && wordListNode.isArray()) {
//                    List<WordDetailDTO> wordList = objectMapper.readValue(
//                            wordListNode.toString(), new TypeReference<List<WordDetailDTO>>() {}
//                    );
//                    wordDTO.setWords(wordList);
//                }
//
//                wordDTOList.add(wordDTO);
//            }
//            wordResponse.setWords(wordDTOList);
//        }
//        return wordResponse;
//    }

    private WordResponse processWordResponse(Long userId, String timeSlotName,
                                             String duration, ResponseEntity<Map> response) throws JsonProcessingException {
        WordResponse wordResponse = new WordResponse();
        wordResponse.setUserId(userId);
        wordResponse.setTimeSlotName(timeSlotName);
        wordResponse.setDuration(duration);

        Map<String, Object> responseBody = response.getBody();

        if (responseBody != null) {
            Map<String, Object> responseResult = (Map<String, Object>) responseBody.get("result");
            Map<String, Object> responseMessage = (Map<String, Object>) responseResult.get("message");
            String jsonResult = (String) responseMessage.get("content");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResult);

            JsonNode interestIdNode = rootNode.get("interestId");
            JsonNode interestNameNode = rootNode.get("interestName");
            JsonNode wordListNode = rootNode.get("wordList");

            if (interestIdNode != null) {
                wordResponse.setInterestId(interestIdNode.asLong());
            }

            if (wordListNode != null && wordListNode.isArray()) {
                List<WordDetailDTO> wordList = objectMapper.readValue(
                        wordListNode.toString(), new TypeReference<List<WordDetailDTO>>() {}
                );
                wordResponse.setWordList(wordList);
            }
        }
        return wordResponse;
    }

    private String cleanJsonString(String json) {
        if (json == null || json.isBlank()) {
            throw new RuntimeException("JSON 응답이 비어 있습니다.");
        }
        json = json.replaceAll("(?i)(입력:|출력:)", "").trim();
        if (!(json.startsWith("{") || json.startsWith("["))) {
            throw new RuntimeException("JSON 형식이 아님: " + json);
        }
        return json;
    }

    private void saveWords(Long userId, WordDTO wordDTO) {
        for (WordDetailDTO wordDetail : wordDTO.getWords()) {
            int retryCount = 0;
            boolean success = false;

            while (retryCount < MAX_RETRIES && !success) {
                try {
                    Word wordEntity = new Word(
                            userId,
                            wordDTO.getInterestId(),
                            wordDetail.getWord(),
                            wordDetail.getMeaning(),
                            wordDetail.getPos(),
                            wordDetail.getEx(),
                            wordDetail.getTr()
                    );
                    wordRepository.save(wordEntity);
                    success = true;
                } catch (HttpClientErrorException.TooManyRequests e) {
                    retryCount++;
                    int waitTime = (int) Math.pow(2, retryCount) * 1000;
                    System.out.println("⏳ 요청 제한 초과(429), " + waitTime + "ms 후 재시도(" + retryCount + "/" + MAX_RETRIES + ")");
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            if (!success) {
                System.err.println("단어 저장 실패: " + wordDetail.getWord());
            }
        }
    }

    private TimeSlotResponse getCurrentTimeSlot(Long userId) {
        LocalTime now = LocalTime.now();
        List<TimeSlotTemplate> timeSlots = timeSlotRepository.findByUserId(userId);

        for (TimeSlotTemplate timeSlot : timeSlots) {
            LocalTime startTime = timeSlot.getStartTime();
            LocalTime endTime = timeSlot.getEndTime();

            if (!now.isBefore(startTime) && now.isBefore(endTime)) {
                return new TimeSlotResponse(timeSlot.getTemplateName(), String.valueOf(ChronoUnit.MINUTES.between(startTime, endTime)));
            }
        }
        return new TimeSlotResponse("자투리 시간", "");
    }
}