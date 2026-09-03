package com.agentflow.conversation.service;

import com.agentflow.agent.service.AgentExecutor;
import com.agentflow.common.exception.AgentFlowException;
import com.agentflow.common.exception.ErrorCode;
import com.agentflow.conversation.dto.MessageCreateRequest;
import com.agentflow.conversation.dto.MessageResponse;
import com.agentflow.conversation.entity.Conversation;
import com.agentflow.conversation.entity.Message;
import com.agentflow.conversation.repository.ConversationRepository;
import com.agentflow.conversation.repository.MessageRepository;
import com.agentflow.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final AgentExecutor agentExecutor;

    @Transactional
    public MessageResponse send(Long userId, Long conversationId, MessageCreateRequest request) {
        Conversation conversation = conversationRepository.findByIdAndUserId(conversationId, userId).orElseThrow(() -> new AgentFlowException(ErrorCode.CONVERSATION_NOT_FOUND));

        Message userMessage = new Message(conversation, UserRole.USER, request.content());

        messageRepository.save(userMessage);

        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Message> messages = messageRepository.findByConversationId(conversationId, pageable);

        Collections.reverse(messages);

        List<org.springframework.ai.chat.messages.Message> chatMessages =
                messages.stream()
                        .map(this::convertMessage)
                        .toList();

        String answer = agentExecutor.execute(conversation.getAgent(), chatMessages);

        Message assistantMessage = new Message(conversation, UserRole.ASSISTANT, answer);

        Message savedMessage = messageRepository.save(assistantMessage);
        return new MessageResponse(savedMessage.getId(), savedMessage.getRole(), savedMessage.getContent());
    }

    private org.springframework.ai.chat.messages.Message convertMessage(Message message) {

        if ("USER".equals(message.getRole())) {
            return new UserMessage(message.getContent());
        }

        if ("ASSISTANT".equals(message.getRole())) {
            return new AssistantMessage(message.getContent());
        }

        throw new IllegalArgumentException("지원하지 않는 메시지 role입니다: " + message.getRole());
    }
}
