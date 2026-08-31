package com.agentflow.conversation.dto;

import jakarta.validation.constraints.NotBlank;

public record MessageCreateRequest(

        @NotBlank(message = "메시지는 필수입니다.")
        String content
) {
}