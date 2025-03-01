package com.bside.potenday.domain.topic.service;

import com.bside.potenday.domain.clova.service.ClovaApiService;
import com.bside.potenday.domain.interest.domain.Interest;
import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.interest.repository.InterestsRepository;
import com.bside.potenday.domain.interest.repository.UserInterestsRepository;
import com.bside.potenday.domain.topic.domain.Topic;
import com.bside.potenday.domain.topic.dto.TopicDTO;
import com.bside.potenday.domain.topic.dto.TopicDetailDTO;
import com.bside.potenday.domain.topic.dto.TopicResponse;
import com.bside.potenday.domain.topic.repository.TopicRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

            List<Topic> existingTopics = topicRepository.findRandomUnpickedTopics(userId, interestId);
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
//        for (Long interestId : interestIds) {
//            List<Topic> existingTopics = topicRepository.findRandomUnpickedTopicsByInterest(userId, interestId, 5);
//            allTopics.addAll(existingTopics);
//
//            int needCount = 5 - existingTopics.size();
//            if (needCount > 0) {
//                TopicResponse clovaResponse = clovaApiService.getClovaTopicResponse(userId, interestId, needCount);
//
//                List<Topic> newTopics = clovaResponse.getTopics().stream()
//                        .map(topicDTO -> new Topic(userId, topicDTO.getInterestId(), topicDTO.getTopicName()))
//                        .collect(Collectors.toList());
//
//                topicRepository.saveAll(newTopics);
//                allTopics.addAll(newTopics);
//            }
//        }

        return convertToTopicResponse(userId, allTopics);
    }


    private TopicResponse convertToTopicResponse(Long userId, List<Topic> topics) {
        List<TopicDTO> topicDTOList = topics.stream()
                .collect(Collectors.groupingBy(Topic::getUserInterestId))
                .entrySet().stream()
                .map(entry -> new TopicDTO(
                        entry.getKey(),
                        entry.getValue().get(0).getTopicName(),
                        entry.getValue().stream()
                                .map(topic -> new TopicDetailDTO(topic.getTopicId(), topic.getTopicName(), topic.getJubjubYn()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new TopicResponse(userId, "닉네임", "자투리 시간", "30", topicDTOList);
    }

    @Transactional
    public void saveTopic(Long userId, Long topicId) {
        Topic topic = topicRepository.findByUserIdAndTopicId(userId, topicId)
                .orElseThrow(() -> new RuntimeException("해당 주제를 찾을 수 없습니다. userId: " + userId + ", topicId: " + topicId));

        topic.updateJubjubStatus();
        topicRepository.save(topic);
    }

    public long getJubjubCountByUser(Long userId) {
        return topicRepository.countJubjubTopicsByUserId(userId);
    }
}
