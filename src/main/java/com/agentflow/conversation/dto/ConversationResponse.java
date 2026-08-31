package com.agentflow.conversation.dto;

public record ConversationResponse(
        Long id,
        Long agentId,
        String title
) {
}