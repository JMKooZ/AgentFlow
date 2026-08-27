package com.agentflow.auth.service;

import com.agentflow.auth.dto.LoginRequest;
import com.agentflow.auth.dto.LoginResponse;
import com.agentflow.auth.entity.RefreshToken;
import com.agentflow.auth.jwt.JwtProperties;
import com.agentflow.auth.jwt.JwtProvider;
import com.agentflow.auth.repository.RefreshTokenRepository;
import com.agentflow.common.exception.AgentFlowException;
import com.agentflow.common.exception.ErrorCode;
import com.agentflow.user.entity.User;
import com.agentflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new AgentFlowException(ErrorCode.INVALID_INPUT));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AgentFlowException(ErrorCode.INVALID_INPUT);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());
        LocalDateTime refreshTokenExpiresAt = LocalDateTime.now().plusSeconds(jwtProperties.refreshTokenExpiration() / 1000);

        refreshTokenRepository.findByUser(user)
                .ifPresentOrElse(existingToken -> existingToken.updateToken(refreshToken, refreshTokenExpiresAt),
                        () -> refreshTokenRepository.save(new RefreshToken(user, refreshToken, refreshTokenExpiresAt)));

        return new LoginResponse(accessToken, refreshToken, user.getId(), user.getEmail(), user.getName());
    }

    @Transactional
    public LoginResponse refreshAccessToken(String refreshToken) {
        // 1. jwt 자체 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new AgentFlowException(ErrorCode.INVALID_INPUT);
        }
        // 2. db에 저장된 refresh token 확인
        RefreshToken savedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AgentFlowException(ErrorCode.INVALID_INPUT));
        // 3. db기준 만료 여부 확인
        if (savedToken.isExpired()) {
            throw new AgentFlowException(ErrorCode.INVALID_INPUT);
        }
        // 4. refresh token과 연결된 사용자 확인
        User user = savedToken.getUser();
        // 5. 새로운 access token 발급
        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        // 6. 새로운 refresh token 생성
        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());
        // 7. 새로운 refresh token 만료시간
        LocalDateTime newRefreshTokenExpiresAt = LocalDateTime.now().plusSeconds(jwtProperties.refreshTokenExpiration() / 1000);
        // 8. 새로운 refresh token 변경
        savedToken.updateToken(newRefreshToken, newRefreshTokenExpiresAt);
        // 9. access, refresh token 반환
        return new LoginResponse(accessToken, newRefreshToken, user.getId(), user.getEmail(), user.getName());
    }
}
