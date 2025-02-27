package com.bside.potenday.domain.feed.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "topic")
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Getter
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id", nullable = false)
    private Long topicId;
    @Column(name = "user_interest_id", nullable = false)
    private Long userInterestId;
    @Column(name = "topic_name", nullable = false)
    private String topicName;
    @Column(name = "rec_at")
    private LocalDate recAt;

    public Topic(Long userInterestId, String topicName) {
        this.userInterestId = userInterestId;
        this.topicName = topicName;
        this.recAt = LocalDate.now();
    }
}
