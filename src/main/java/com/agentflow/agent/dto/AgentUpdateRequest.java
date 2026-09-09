package com.agentflow.agent.dto;

import com.agentflow.agent.tool.AgentToolType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record AgentUpdateRequest(

        @NotBlank(message = "Agent 이름은 필수입니다.")
        @Size(max = 100, message = "Agent 이름은 100자 이하여야 합니다.")
        String name,

        @Size(max = 500, message = "Agent 설명은 500자 이하여야 합니다.")
        String description,

        @NotBlank(message = "System Prompt는 필수입니다.")
        String systemPrompt,

        Set<AgentToolType> tools
) {
}