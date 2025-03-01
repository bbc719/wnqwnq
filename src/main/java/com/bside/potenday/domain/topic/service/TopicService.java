package com.bside.potenday.domain.topic.service;

import com.bside.potenday.domain.clova.service.ClovaApiService;
import com.bside.potenday.domain.interest.domain.Interest;
import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.interest.repository.InterestsRepository;
import com.bside.potenday.domain.interest.repository.UserInterestsRepository;
import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.timeSlot.repository.TimeSlotRepository;
import com.bside.potenday.domain.topic.domain.Topic;
import com.bside.potenday.domain.topic.dto.JubjubSummaryResponse;
import com.bside.potenday.domain.topic.dto.TopicDTO;
import com.bside.potenday.domain.topic.dto.TopicDetailDTO;
import com.bside.potenday.domain.topic.dto.TopicResponse;
import com.bside.potenday.domain.topic.repository.TopicRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    @Autowired
    private final TopicRepository topicRepository;
    @Autowired
    private final InterestsRepository interestsRepository;
    @Autowired
    private final UserInterestsRepository userInterestsRepository;
    @Autowired
    private final TimeSlotRepository timeSlotRepository;
    @Autowired
    private final ClovaApiService clovaApiService;

    @Transactional
    public TopicResponse getTopics(Long userId) throws JsonProcessingException {
        List<UserInterest> userInterests = userInterestsRepository.findByUserId(userId);
        List<Interest> userInterestsList = interestsRepository.findByInterestIdIn(
                userInterests.stream().map(UserInterest::getInterestId).collect(Collectors.toList()));
        List<Long> interestIds = userInterestsList.stream()
                .map(Interest::getInterestId)
                .toList();
        List<String> interestNames = userInterestsList.stream()
                .map(Interest::getInterestName)
                .toList();

        List<Topic> allTopics = new ArrayList<>();

        for (int i = 0; i < interestNames.size(); i++) {
            Long interestId = interestIds.get(i);
            String interestName = interestNames.get(i);

            List<Topic> existingTopics = topicRepository.findOldestUnpickedTopics(userId, interestId);
            allTopics.addAll(existingTopics);

            int needCount = 5 - existingTopics.size();
            if (needCount > 0) {
                TopicResponse clovaResponse = clovaApiService.getClovaTopicResponse(userId, interestId, interestName, needCount);

                List<Topic> newTopics = clovaResponse.getTopics().stream()
                        .flatMap(topicDTO -> topicDTO.getTopics().stream()
                                .map(topicDetailDTO -> new Topic(userId, topicDTO.getInterestId(), topicDetailDTO.getTopicName()))
                        )
                        .collect(Collectors.toList());

                topicRepository.saveAll(newTopics);
                allTopics.addAll(newTopics);
            }
        }
        return convertToTopicResponse(userId, allTopics);
    }

    private TopicResponse convertToTopicResponse(Long userId, List<Topic> topics) {
        if (topics.isEmpty()) {
            return new TopicResponse(userId, "닉네임", "자투리 시간", "30", new ArrayList<>());
        }

        Map<Long, String> interestIdToNameMap = userInterestsRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(UserInterest::getInterestId, userInterest ->
                        interestsRepository.findById(userInterest.getInterestId())
                                .map(Interest::getInterestName)
                                .orElse("알 수 없는 관심사")
                ));

        List<TopicDTO> topicDTOList = topics.stream()
                .collect(Collectors.groupingBy(Topic::getUserInterestId))
                .entrySet().stream()
                .map(entry -> new TopicDTO(
                        entry.getKey(), // 관심사 ID
                        interestIdToNameMap.getOrDefault(entry.getKey(), "알 수 없는 관심사"),
                        entry.getValue().stream()
                                .map(topic -> new TopicDetailDTO(topic.getTopicId(), topic.getTopicName(), topic.getJubjubYn()))
                                .collect(Collectors.toList()) // 🔥 topicName이 올바르게 설정됨
                ))
                .collect(Collectors.toList());

        return new TopicResponse(userId, "닉네임", "자투리 시간", "30", topicDTOList);
    }

    public long getJubjubCountByUser(Long userId) {
        return topicRepository.countJubjubTopicsByUserId(userId);
    }

    @Transactional
    public void completeJubjub(Long userId, Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("해당 주제를 찾을 수 없습니다. topicId: " + topicId));

        if (!topic.getUserId().equals(userId)) {
            throw new RuntimeException("해당 주제에 대한 접근 권한이 없습니다.");
        }

        topic.completeJubjub();
        topicRepository.save(topic);
    }

    public long getTodayJubjubCount(Long userId) {
        return topicRepository.countTodayJubjubByUserId(userId);
    }

    public JubjubSummaryResponse getTodayJubjubSummary(Long userId) {
        long jubjubCount = topicRepository.countTodayJubjubByUserId(userId);
        long totalJubjubTime = topicRepository.getTodayJubjubTimeByUserId(userId);

        return new JubjubSummaryResponse(userId, jubjubCount, totalJubjubTime);
    }
}
