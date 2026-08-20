package com.agentflow.user.service;

import com.agentflow.user.entity.User;
import com.agentflow.user.entity.UserRole;
import com.agentflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(
            String email,
            String password,
            String name
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = User.create(
                email,
                password,
                name,
                UserRole.USER
        );

        return userRepository.save(user);
    }
}