package com.agentflow.auth.service;

import com.agentflow.auth.dto.LoginRequest;
import com.agentflow.auth.dto.LoginResponse;
import com.agentflow.auth.jwt.JwtProvider;
import com.agentflow.user.entity.User;
import com.agentflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));
        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new IllegalArgumentException(
                    "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());

        return new LoginResponse(
                accessToken,
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }
}
