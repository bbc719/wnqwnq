package com.bside.potenday.domain.topic.repository;

import com.bside.potenday.domain.topic.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findDistinctByUserInterestId(Long userInterestId);

    Optional<Topic> findByUserIdAndTopicId(Long userId, Long topicId);

    @Query(value = "SELECT * FROM topic t WHERE t.user_id = :userId AND t.user_interest_id = :interestId AND t.jubjub_yn = false ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Topic> findRandomUnpickedTopics(@Param("userId") Long userId, @Param("interestId") Long interestId);

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.userId = :userId AND t.jubjubYn = true")
    long countJubjubTopicsByUserId(@Param("userId") Long userId);
}
