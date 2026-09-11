package com.agentflow.agent.dto;

import com.agentflow.agent.entity.Agent;
import com.agentflow.tool.AgentToolType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public record AgentResponse(
        Long id,
        Long userId,
        String name,
        String description,
        String systemPrompt,
        Set<AgentToolType> tools,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AgentResponse from(Agent agent) {
        return new AgentResponse(
                agent.getId(),
                agent.getUser().getId(),
                agent.getName(),
                agent.getDescription(),
                agent.getSystemPrompt(),
                new HashSet<>(agent.getEnabledTools()),
                agent.getCreatedAt(),
                agent.getUpdatedAt()
        );
    }
}