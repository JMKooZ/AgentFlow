package com.agentflow.conversation.service;

import com.agentflow.conversation.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationSummaryService {
    private static final String SUMMARY_SYSTEM_PROMPT = """
            당신은 대화 내용을 요약하는 역할을 합니다.
            
            다음 대화에서 이후 대화를 이어가기 위해 필요한 핵심 정보만 요약하세요.
            
            반드시 다음 내용을 중심으로 요약합니다.
            - 사용자의 주요 목적
            - 사용자가 요청한 내용
            - 이미 결정된 사항
            - 중요한 기술적 맥락
            - 대화에서 유지해야 하는 정보
            
            이전 요약이 함께 주어지면, 이전 요약 내용을 유지하면서 새로운 대화 내용을 반영해
            하나의 누적된 요약으로 갱신하세요.
            
            불필요한 인사말이나 반복적인 표현은 제거하세요.
            요약은 간결하지만 이후 대화의 맥락을 이해할 수 있어야 합니다.
            """;

    private final ChatClient client;

    public String summarize(List<Message> messages) {
        return summarize(null, messages);
    }

    public String summarize(String previousSummary, List<Message> newMessages) {
        String newConversationText = newMessages.stream().map(this::formatMessage).reduce("", (a, b) -> a + b);

        String userPrompt = (previousSummary == null || previousSummary.isBlank())
                ? newConversationText
                : "[이전 요약]\n" + previousSummary + "\n\n[이어지는 대화]\n" + newConversationText;

        return client
                .prompt()
                .system(SUMMARY_SYSTEM_PROMPT)
                .user(userPrompt)
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
