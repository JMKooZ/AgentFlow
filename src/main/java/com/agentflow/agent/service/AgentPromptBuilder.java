package com.agentflow.agent.service;

import com.agentflow.agent.entity.Agent;
import org.springframework.stereotype.Component;

@Component
public class AgentPromptBuilder {

    public String build(Agent agent) {
        return agent.getSystemPrompt();
    }
}