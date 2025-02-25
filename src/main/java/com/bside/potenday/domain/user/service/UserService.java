package com.bside.potenday.domain.user.service;

import com.bside.potenday.domain.user.domain.Job;
import com.bside.potenday.domain.user.domain.User;
import com.bside.potenday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void updateNickname(Long userId, String nickname, Job job) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.updateNickName(nickname, job);
    }
}
