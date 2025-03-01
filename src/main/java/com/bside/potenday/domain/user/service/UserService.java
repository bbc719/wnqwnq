package com.bside.potenday.domain.user.service;

import com.bside.potenday.domain.user.domain.User;
import com.bside.potenday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    public Boolean getIsCompleted(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId : " + userId));
        return user.isCompleted();
    }

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId : " + userId));
        user.updateNickName(nickname);
    }
}
