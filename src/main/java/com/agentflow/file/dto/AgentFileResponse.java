package com.agentflow.file.dto;

import com.agentflow.file.entity.AgentFile;

import java.time.LocalDateTime;

public record AgentFileResponse(
        Long id,
        String fileName,
        long sizeBytes,
        LocalDateTime createdAt
) {
    public static AgentFileResponse from(AgentFile agentFile) {
        return new AgentFileResponse(
                agentFile.getId(),
                agentFile.getFileName(),
                agentFile.getSizeBytes(),
                agentFile.getCreatedAt()
        );
    }
}