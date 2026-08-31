package com.agentflow.conversation.dto;

public record MessageResponse(
        Long id,
        String role,
        String content
) {
}