package com.agentflow.auth.dto;

public record LoginRequest(
        String email,
        String password
) {
}
