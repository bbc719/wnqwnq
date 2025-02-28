package com.bside.potenday.domain.feed.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "topic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long topicId;
    @Column(name = "user_id", nullable = false)
    @JsonProperty("userId")
    private Long userId;
    @Column(name = "user_interest_id", nullable = false)
    @JsonProperty("userInterestId")
    private Long userInterestId;
    @Column(name = "topic_name", nullable = false)
    @JsonProperty("topicName")
    private String topicName;
    @Column(name = "rec_at")
    private LocalDateTime recAt;

    public Topic(Long userId, Long userInterestId, String topicName) {
        this.userId = userId;
        this.userInterestId = userInterestId;
        this.topicName = topicName;
        this.recAt = LocalDateTime.now();
    }
}
