package com.agentflow.agent.service;

import com.agentflow.agent.entity.Agent;
import org.springframework.stereotype.Component;

@Component
public class AgentPromptBuilder {

    public String build(Agent agent) {
        return agent.getSystemPrompt();
    }

    public String build(Agent agent, String summary) {
        if (summary == null || summary.isBlank()) {
            return agent.getSystemPrompt();
        }
        return agent.getSystemPrompt() + "\n\n[이전 대화 요약]\n" + summary;
    }
}