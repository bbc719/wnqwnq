package com.bside.potenday.domain.feed.repository;

import com.bside.potenday.domain.interest.domain.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<UserInterest, Long> {

    UserInterest findByUserIdAndInterestId(Long userId, Long interestId);
}
