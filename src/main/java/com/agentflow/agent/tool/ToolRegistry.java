package com.agentflow.agent.tool;

import com.agentflow.conversation.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ToolRegistry {

    private final DateTimeTool dateTimeTool;
    private final CalculatorTool calculatorTool;
    private final ConversationRepository conversationRepository;

    public Object[] resolve(Set<AgentToolType> enabledTools, Long userId) {
        if (enabledTools == null || enabledTools.isEmpty()) {
            return new Object[0];
        }

        return enabledTools.stream()
                .map(type -> resolveOne(type, userId))
                .toArray();
    }

    private Object resolveOne(AgentToolType type, Long userId) {
        return switch (type) {
            case DATE_TIME -> dateTimeTool;
            case CALCULATOR -> calculatorTool;
            case CONVERSATION_QUERY -> new ConversationQueryTool(userId, conversationRepository);
        };
    }
}