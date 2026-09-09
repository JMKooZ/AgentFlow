package com.agentflow.tool;

import com.agentflow.tool.calculator.CalculatorTool;
import com.agentflow.tool.conversation.ConversationQueryTool;
import com.agentflow.tool.datetime.DateTimeTool;
import com.agentflow.tool.file.FileWriteTool;
import com.agentflow.tool.web.WebSearchTool;
import com.agentflow.conversation.repository.ConversationRepository;
import com.agentflow.file.repository.AgentFileRepository;
import com.agentflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ToolRegistry {

    private final DateTimeTool dateTimeTool;
    private final CalculatorTool calculatorTool;
    private final WebSearchTool webSearchTool;
    private final ConversationRepository conversationRepository;
    private final AgentFileRepository agentFileRepository;
    private final UserRepository userRepository;

    @Value("${file.storage.base-dir:./storage}")
    private String baseStorageDir;

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
            case WEB_SEARCH -> webSearchTool;
            case CONVERSATION_QUERY -> new ConversationQueryTool(userId, conversationRepository);
            case FILE_WRITE -> new FileWriteTool(userId, baseStorageDir, agentFileRepository, userRepository);
        };
    }
}