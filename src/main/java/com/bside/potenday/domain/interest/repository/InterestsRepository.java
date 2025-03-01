package com.bside.potenday.domain.interest.repository;

import com.bside.potenday.domain.interest.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestsRepository extends JpaRepository<Interest, Long> {
    List<Interest> findByInterestIdIn(List<Long> interestIds);
}
