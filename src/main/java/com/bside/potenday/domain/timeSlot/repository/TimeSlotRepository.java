package com.bside.potenday.domain.timeSlot.repository;

import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlotTemplate, Long> {

    List<TimeSlotTemplate> findByUserId(Long userId);
}
