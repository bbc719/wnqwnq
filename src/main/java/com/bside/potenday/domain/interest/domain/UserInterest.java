package com.bside.potenday.domain.interest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_interest")
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Getter
public class UserInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "user_interest_id")
    private Long userInterestId;
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    @Column(name = "interest_id", nullable = false, unique = true)
    private Long interestId;

    public UserInterest(Long userInterestId, Long userId, Long interestId) {
        this.userInterestId = userInterestId;
        this.userId = userId;
        this.interestId = interestId;
    }
}