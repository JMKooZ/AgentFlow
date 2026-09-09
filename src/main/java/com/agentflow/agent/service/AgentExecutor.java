package com.agentflow.agent.service;

import com.agentflow.agent.entity.Agent;
import com.agentflow.tool.ToolRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AgentExecutor {

    private final ChatClient chatClient;
    private final AgentPromptBuilder agentPromptBuilder;
    private final ToolRegistry toolRegistry;

    public String execute(Agent agent, String message, Long userId) {
        return chatClient
                .prompt()
                .system(agentPromptBuilder.build(agent))
                .user(message)
                .tools(toolRegistry.resolve(agent.getEnabledTools(), userId))
                .call()
                .content();
    }

    public String execute(Agent agent, List<Message> messages, String summary, Long userId) {
        return chatClient
                .prompt()
                .system(agentPromptBuilder.build(agent, summary))
                .messages(messages)
                .tools(toolRegistry.resolve(agent.getEnabledTools(), userId))
                .call()
                .content();
    }
}