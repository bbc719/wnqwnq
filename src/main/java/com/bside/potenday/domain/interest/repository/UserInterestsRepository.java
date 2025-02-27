package com.bside.potenday.domain.interest.repository;

import com.bside.potenday.domain.interest.domain.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestsRepository extends JpaRepository<UserInterest, Long> {
    List<UserInterest> findByUserId(Long userId);

    Boolean existsByUserIdAndInterestId(Long userId, Long interestId);
}
