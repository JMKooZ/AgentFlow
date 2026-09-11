package com.agentflow.tool.conversation;

import com.agentflow.conversation.entity.Conversation;
import com.agentflow.conversation.repository.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ConversationQueryTool {

    private static final int MAX_RESULTS = 10;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Long userId;
    private final ConversationRepository conversationRepository;

    public ConversationQueryTool(Long userId, ConversationRepository conversationRepository) {
        this.userId = userId;
        this.conversationRepository = conversationRepository;
    }

    @Tool(description = "현재 사용자가 만든 대화(Conversation) 목록을 최신순으로 조회한다.")
    public String listMyConversations() {
        List<Conversation> conversations = conversationRepository.findAllByUserId(userId);

        log.info("[Tool 호출] listMyConversations(userId={}) -> {}건", userId, conversations.size());

        if (conversations.isEmpty()) {
            return "아직 생성된 대화가 없습니다.";
        }

        return conversations.stream()
                .sorted(Comparator.comparing(Conversation::getCreatedAt).reversed())
                .limit(MAX_RESULTS)
                .map(c -> "- [%d] %s (%s)".formatted(c.getId(), c.getTitle(), c.getCreatedAt().format(FORMATTER)))
                .collect(Collectors.joining("\n"));
    }
}