package com.agentflow.conversation.service;

import com.agentflow.conversation.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationSummaryService {
    private final ChatClient client;

    public String summarize(List<Message> messages) {
        String conversationText = messages.stream().map(this::formatMessage).reduce("", (a, b) -> a + b);

        return client
                .prompt()
                .system(
                        """
                                당신은 대화 내용을 요약하는 역할을 합니다.
                                
                                다음 대화에서 이후 대화를 이어가기 위해 필요한 핵심 정보만 요약하세요.
                                
                                반드시 다음 내용을 중심으로 요약합니다.
                                - 사용자의 주요 목적
                                - 사용자가 요청한 내용
                                - 이미 결정된 사항
                                - 중요한 기술적 맥락
                                - 대화에서 유지해야 하는 정보
                                
                                불필요한 인사말이나 반복적인 표현은 제거하세요.
                                요약은 간결하지만 이후 대화의 맥락을 이해할 수 있어야 합니다.
                                """)
                .user(conversationText)
                .call()
                .content();
    }

    private String formatMessage(Message message) {
        return switch (message.getRole()) {
            case "USER" -> "USER: " + message.getContent() + "\n";
            case "ASSISTANT" -> "ASSISTANT: " + message.getContent() + "\n";
            default -> "";
        };
    }
}
