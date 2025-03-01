package com.bside.potenday.domain.feed.service;

import com.bside.potenday.domain.topic.domain.Topic;
import com.bside.potenday.domain.feed.dto.TimeSlotResponse;
import com.bside.potenday.domain.feed.repository.FeedRepository;
import com.bside.potenday.domain.topic.repository.TopicRepository;
import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.timeSlot.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private TopicRepository TopicRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    public TimeSlotResponse getCurrentTimeSlot(Long userId) {
        // 현재 시간 가져오기
        LocalTime now = LocalTime.now();

        // 사용자가 설정한 자투리 시간 목록 가져오기
        List<TimeSlotTemplate> timeSlots = timeSlotRepository.findAll();

        // 현재 시간이 자투리 시간에 속하는지 확인
        for (TimeSlotTemplate timeSlot : timeSlots) {
            LocalTime startTime = timeSlot.getStartTime();
            LocalTime endTime = timeSlot.getEndTime();

            if (!now.isBefore(startTime) && now.isBefore(endTime)) {
                return new TimeSlotResponse(timeSlot.getTemplateName(), String.valueOf(ChronoUnit.MINUTES.between(startTime, endTime)));
            }
        }

        // 어느 자투리 시간에도 속하지 않을 경우
        return new TimeSlotResponse("자투리 시간", "");
    }

    public LocalDateTime getRecAt(Long userId, Long interestId) {
        UserInterest userInterestId = feedRepository.findByUserIdAndInterestId(userId, interestId);

        Optional<Topic> topic = TopicRepository.findDistinctByUserInterestId(userInterestId.getUserInterestId());

        if (topic.isPresent()) {
            return topic.get().getRecAt();
        } else {
            return LocalDateTime.now();
        }
    }
}
