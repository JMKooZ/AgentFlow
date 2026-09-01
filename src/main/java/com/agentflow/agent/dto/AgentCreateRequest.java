package com.agentflow.agent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AgentCreateRequest(

        @NotBlank(message = "Agent 이름은 필수입니다.")
        @Size(max = 100, message = "Agent 이름은 100자 이하여야 합니다.")
        String name,

        @Size(max = 500, message = "Agent 설명은 500자 이하여야 합니다.")
        String description,

        String systemPrompt
) {
}
