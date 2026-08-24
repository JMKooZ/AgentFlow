package com.agentflow.user.service;

import com.agentflow.common.exception.AgentFlowException;
import com.agentflow.common.exception.ErrorCode;
import com.agentflow.user.entity.User;
import com.agentflow.user.entity.UserRole;
import com.agentflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(
            String email,
            String password,
            String name
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new AgentFlowException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.create(
                email,
                encodedPassword,
                name,
                UserRole.USER
        );

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AgentFlowException(ErrorCode.USER_NOT_FOUND));
    }
}