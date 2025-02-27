package com.bside.potenday.domain.timeSlot.service;

import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.timeSlot.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Transactional
    public void createTimeSlot(List<TimeSlotTemplate> timeSlot) {
        timeSlotRepository.saveAll(timeSlot);
    }
}
