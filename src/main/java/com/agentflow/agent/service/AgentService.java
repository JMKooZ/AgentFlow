package com.agentflow.agent.service;

import com.agentflow.agent.dto.*;
import com.agentflow.agent.entity.Agent;
import com.agentflow.agent.repository.AgentRepository;
import com.agentflow.common.exception.AgentFlowException;
import com.agentflow.common.exception.ErrorCode;
import com.agentflow.user.entity.User;
import com.agentflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgentService {

    private final AgentRepository agentRepository;
    private final UserRepository userRepository;
    private final AgentExecutor agentExecutor;

    @Transactional
    public AgentResponse create(Long userId, AgentCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AgentFlowException(ErrorCode.USER_NOT_FOUND));

        Agent agent = new Agent(user, request.name(), request.description(), request.systemPrompt());

        agentRepository.save(agent);
        return AgentResponse.from(agent);
    }

    public List<AgentResponse> findMyAgents(Long userId) {
        return agentRepository.findAllByUserId(userId)
                .stream()
                .map(AgentResponse::from)
                .toList();
    }

    public AgentResponse find(Long userId, Long agentId) {
        Agent agent = findAgent(agentId, userId);
        return AgentResponse.from(agent);
    }

    @Transactional
    public AgentResponse update(Long userId, Long agentId, AgentUpdateRequest request) {
        Agent agent = findAgent(agentId, userId);
        agent.update(request.name(), request.description(), request.systemPrompt());
        return AgentResponse.from(agent);
    }

    @Transactional
    public void delete(Long userId, Long agentId) {
        Agent agent = findAgent(agentId, userId);
        agentRepository.delete(agent);
    }

    public AgentExecuteResponse execute(Long userId, Long agentId, AgentExecuteRequest request) {
        Agent agent = findAgent(agentId, userId);
        String answer = agentExecutor.execute(agent, request.message());
        return new AgentExecuteResponse(answer);
    }

    private Agent findAgent(Long agentId, Long userId) {
        return agentRepository.findByIdAndUserId(agentId, userId)
                .orElseThrow(() -> new AgentFlowException(ErrorCode.AGENT_NOT_FOUND));
    }
}
