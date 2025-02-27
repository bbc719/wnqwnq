package com.bside.potenday.domain.interest.repository;

import com.bside.potenday.domain.interest.domain.Interest;
import com.bside.potenday.domain.interest.domain.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestsRepository extends JpaRepository<Interest, Long> {
    Interest findByInterestId(Long userId);
}
