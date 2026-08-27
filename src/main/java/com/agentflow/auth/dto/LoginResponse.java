package com.agentflow.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        Long userId,
        String email,
        String name
) {
}
