package com.bside.potenday.domain.timeSlot.service;

import com.bside.potenday.domain.timeSlot.domain.TimeSlotTemplate;
import com.bside.potenday.domain.timeSlot.repository.TimeSlotRepository;
import com.bside.potenday.domain.user.domain.User;
import com.bside.potenday.domain.user.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createOrUpdateTimeSlot(Long userId, List<TimeSlotTemplate> timeSlots) {
        for (TimeSlotTemplate timeSlot : timeSlots) {
            if (timeSlot.getTemplateId() == null) {
                timeSlotRepository.save(timeSlot);
            } else {
                TimeSlotTemplate existingSlot = timeSlotRepository.findById(timeSlot.getTemplateId())
                        .orElseThrow(() -> new RuntimeException("해당 타임 슬롯을 찾을 수 없습니다."));

                existingSlot.updateSlot(timeSlot.getTemplateName(), timeSlot.getStartTime(), timeSlot.getEndTime());
                timeSlotRepository.save(existingSlot);
            }
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.updateIsCompleted();
        userRepository.save(user);
    }

}
