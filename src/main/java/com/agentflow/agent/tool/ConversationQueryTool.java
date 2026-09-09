package com.agentflow.agent.tool;

import com.agentflow.conversation.entity.Conversation;
import com.agentflow.conversation.repository.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Bean(@Component)이 아니다.
 * userId는 LLM이 넘기는 파라미터가 아니라, ToolRegistry가 요청마다
 * "로그인한 사용자 ID"로 직접 주입해서 새로 생성하는 인스턴스다.
 * 이렇게 해야 다른 사용자의 Conversation을 조회하도록 유도당할 여지가 없다.
 */
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

    @Tool(description = "현재 사용자가 만든 대화(Conversation) 목록을 최신순으로 조회한다. " +
            "사용자가 '내가 만든 대화 뭐 있어', '최근에 무슨 대화했지' 등을 물어볼 때 사용한다.")
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