package com.agentflow.conversation.dto;

import jakarta.validation.constraints.NotBlank;

public record ConversationCreateRequest(
        @NotBlank(message = "대화 제목은 필수입니다.")
        String title
) {
}
