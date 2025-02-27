package com.bside.potenday.domain.feed.service;

import com.bside.potenday.domain.feed.domain.Topic;
import com.bside.potenday.domain.feed.repository.FeedRepository;
import com.bside.potenday.domain.feed.repository.TopicRepository;
import com.bside.potenday.domain.interest.domain.UserInterest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private TopicRepository TopicRepository;

    public LocalDate getRecAt(Long userId, Long interestId) {
        UserInterest userInterestId = feedRepository.findByUserIdAndInterestId(userId, interestId);

        Optional<Topic> topic = TopicRepository.findDistinctByUserInterestId(userInterestId.getUserInterestId());

        if (topic.isPresent()) {
            return topic.get().getRecAt();
        } else {
            return LocalDate.now();
        }
    }
}
