package com.bside.potenday.domain.interest.service;

import com.bside.potenday.domain.interest.domain.Interest;
import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.interest.repository.InterestsRepository;
import com.bside.potenday.domain.interest.repository.UserInterestsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestsService {

    @Autowired
    private InterestsRepository interestsRepository;
    @Autowired
    private UserInterestsRepository userInterestsRepository;

    public List<Interest> getInterests() {
        return interestsRepository.findAll();
    }

    public void saveUserInterest(List<UserInterest> userInterests) {
        List<UserInterest> newInterests = new ArrayList<>();

        for (UserInterest userInterest : userInterests) {
            boolean exists = userInterestsRepository.existsByUserIdAndInterestId(
                    userInterest.getUserId(), userInterest.getInterestId()
            );

            if (!exists) { // 존재하지 않을 경우에만 추가
                newInterests.add(userInterest);
            }
        }

        if (!newInterests.isEmpty()) {
            userInterestsRepository.saveAll(newInterests);
        }
        //userInterestsRepository.saveAll(userInterests);
    }

    public List<UserInterest> getUserInterest(Long userId) {
        return userInterestsRepository.findByUserId(userId);
    }
}
