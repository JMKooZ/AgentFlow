package com.agentflow.agent.dto;

import jakarta.validation.constraints.NotBlank;

public record AgentExecuteRequest(

        @NotBlank(message = "메세지는 필수입니다.")
        String message
) {
}
