package com.agentflow.conversation.service;

import com.agentflow.agent.entity.Agent;
import com.agentflow.agent.repository.AgentRepository;
import com.agentflow.common.exception.AgentFlowException;
import com.agentflow.common.exception.ErrorCode;
import com.agentflow.conversation.dto.ConversationCreateRequest;
import com.agentflow.conversation.dto.ConversationResponse;
import com.agentflow.conversation.entity.Conversation;
import com.agentflow.conversation.repository.ConversationRepository;
import com.agentflow.user.entity.User;
import com.agentflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;

    public List<ConversationResponse> findAllByAgent(Long userId, Long agentId) {
        return conversationRepository.findAllByAgentIdAndUserId(agentId, userId).stream()
                .map(c -> new ConversationResponse(c.getId(), c.getAgent().getId(), c.getTitle()))
                .toList();
    }

    @Transactional
    public ConversationResponse create(Long userId, Long agentId, ConversationCreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AgentFlowException(ErrorCode.USER_NOT_FOUND));
        Agent agent = agentRepository.findByIdAndUserId(agentId, userId).orElseThrow(() -> new AgentFlowException(ErrorCode.AGENT_NOT_FOUND));
        Conversation conversation = new Conversation(user, agent, request.title());
        Conversation saved = conversationRepository.save(conversation);

        return new ConversationResponse(saved.getId(), agent.getId(), saved.getTitle());
    }

    @Transactional
    public void delete(Long userId, Long conversationId) {
        Conversation conversation = conversationRepository.findByIdAndUserId(conversationId, userId).orElseThrow(() -> new AgentFlowException(ErrorCode.CONVERSATION_NOT_FOUND));
        conversationRepository.delete(conversation);
    }
}
