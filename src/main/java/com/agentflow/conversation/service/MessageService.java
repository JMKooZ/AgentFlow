package com.agentflow.conversation.service;

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
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ChatClient chatClient;

    @Transactional
    public MessageResponse send(Long userId, Long conversationId, MessageCreateRequest request) {
        Conversation conversation = conversationRepository.findByIdAndUserId(conversationId, userId).orElseThrow(() -> new AgentFlowException(ErrorCode.CONVERSATION_NOT_FOUND));

//        List<Message> previousMessages = messageRepository.findAllByConversationIdOrderByCreatedAtAsc(conversationId);

        Message userMessage = new Message(conversation, UserRole.USER, request.content());

        messageRepository.save(userMessage);

        List<Message> messages = messageRepository.findAllByConversationIdOrderByCreatedAtAsc(conversationId);

        List<org.springframework.ai.chat.messages.Message> chatMessages =
                messages.stream()
                        .map(this::convertMessage)
                        .toList();

        String systemPrompt = """
                너는 %s라는 AI Agent다.

                너의 역할:
                %s
                """.formatted(
                conversation.getAgent().getName(),
                conversation.getAgent().getDescription()
        );

//        for (Message message : previousMessages) {
//            if (UserRole.USER.equals(message.getRole())) {
//                chatMessages.add(new UserMessage(message.getContent()));
//            } else if (UserRole.ASSISTANT.equals(message.getRole())) {
//                chatMessages.add(new AssistantMessage(message.getContent()));
//            }
//
//            chatMessages.add(new UserMessage(request.content()));
//        }

        String answer = chatClient
                .prompt()
                .system(systemPrompt)
                .messages(chatMessages)
                .call()
                .content();

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
