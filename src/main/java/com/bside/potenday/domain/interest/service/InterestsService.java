package com.bside.potenday.domain.interest.service;

import com.bside.potenday.domain.interest.domain.Interest;
import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.interest.repository.InterestsRepository;
import com.bside.potenday.domain.interest.repository.UserInterestsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void saveUserInterest(Long userId, List<Long> interestIds) {
        List<UserInterest> updatedInterests = new ArrayList<>();

        for (Long interestId : interestIds) {
            UserInterest userInterest = userInterestsRepository.findByUserIdAndInterestId(userId, interestId);

            if (userInterest == null) {
                userInterest = new UserInterest(userId, interestId);
                updatedInterests.add(userInterest);
            }
        }

        if (!updatedInterests.isEmpty()) {
            userInterestsRepository.saveAll(updatedInterests);
        }
    }

    public List<UserInterest> getUserInterest(Long userId) {
        return userInterestsRepository.findByUserId(userId);
    }
}
