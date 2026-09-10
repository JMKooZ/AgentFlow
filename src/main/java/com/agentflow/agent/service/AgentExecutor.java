package com.agentflow.agent.service;

import com.agentflow.agent.entity.Agent;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AgentExecutor {

    private final ChatClient chatClient;
    private final AgentPromptBuilder agentPromptBuilder;

    public String execute(Agent agent, String message) {
        return chatClient
                .prompt()
                .system(agentPromptBuilder.build(agent))
                .user(message)
                .call()
                .content();
    }

    public String execute(Agent agent, List<Message> messages) {
        return execute(agent, messages, null);
    }

    public String execute(Agent agent, List<Message> messages, String summary) {
        return chatClient
                .prompt()
                .system(agentPromptBuilder.build(agent, summary))
                .messages(messages)
                .call()
                .content();
    }

    public Flux<String> stream(Agent agent, List<Message> messages, String summary) {
        return chatClient
                .prompt()
                .system(agentPromptBuilder.build(agent, summary))
                .messages(messages)
                .stream()
                .content();
    }
}
