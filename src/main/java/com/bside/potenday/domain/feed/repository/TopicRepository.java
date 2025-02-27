package com.bside.potenday.domain.feed.repository;

import com.bside.potenday.domain.feed.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    public Optional<Topic> findDistinctByUserInterestId(Long userInterestId);


}
