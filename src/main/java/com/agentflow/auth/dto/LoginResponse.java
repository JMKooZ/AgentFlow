package com.agentflow.auth.dto;

public record LoginResponse(
        String accessToken,
        Long userId,
        String email,
        String name
) {
}
