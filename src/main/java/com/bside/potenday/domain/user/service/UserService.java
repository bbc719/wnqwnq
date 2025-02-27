package com.bside.potenday.domain.user.service;

import com.bside.potenday.domain.interest.domain.UserInterest;
import com.bside.potenday.domain.user.domain.User;
import com.bside.potenday.domain.user.dto.UserProfileRequest;
import com.bside.potenday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void updateNickname(UserProfileRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        user.updateNickName(request.getNickname());
    }

    @Transactional
    public void saveInterest(UserInterest request) {

    }
}
