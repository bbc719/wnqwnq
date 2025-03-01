package com.bside.potenday.domain.topic.repository;

import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.topic.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findDistinctByUserInterestId(Long userInterestId);

    Optional<Topic> findByUserIdAndTopicId(Long userId, Long topicId);

    @Query(value = "SELECT * FROM topic t WHERE t.user_id = :userId AND t.user_interest_id = :interestId AND t.jubjub_yn = false ORDER BY t.rec_at ASC LIMIT 5", nativeQuery = true)
    List<Topic> findOldestUnpickedTopics(@Param("userId") Long userId, @Param("interestId") Long interestId);

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.userId = :userId AND t.jubjubYn = true")
    long countJubjubTopicsByUserId(@Param("userId") Long userId);

    List<TimeSlotTemplate> findByUserId(Long userId);

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.userId = :userId " +
            "AND DATE(t.jubjubDate) = CURRENT_DATE")
    long countTodayJubjubByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) * 10 FROM Topic t WHERE t.userId = :userId " +
            "AND DATE(t.jubjubDate) = CURRENT_DATE")
    long getTodayJubjubTimeByUserId(@Param("userId") Long userId);
}

