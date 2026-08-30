package com.agentflow.agent.dto;

import com.agentflow.agent.entity.Agent;

import java.time.LocalDateTime;

public record AgentResponse(
        Long id,
        Long userId,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AgentResponse from(Agent agent) {
        return new AgentResponse(agent.getId(), agent.getUser().getId(), agent.getName(), agent.getDescription(), agent.getCreatedAt(), agent.getUpdatedAt());
    }
}
