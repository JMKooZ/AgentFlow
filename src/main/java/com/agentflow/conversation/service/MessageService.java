package com.agentflow.conversation.service;

import com.agentflow.agent.service.AgentExecutor;
import com.agentflow.common.exception.AgentFlowException;
import com.agentflow.common.exception.ErrorCode;
import com.agentflow.conversation.dto.MessageCreateRequest;
import com.agentflow.conversation.dto.MessageResponse;
import com.agentflow.conversation.entity.Conversation;
import com.agentflow.conversation.entity.Message;
import com.agentflow.conversation.entity.MessageRole;
import com.agentflow.conversation.repository.ConversationRepository;
import com.agentflow.conversation.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private static final int RECENT_MESSAGE_LIMIT = 20;
    private static final int SUMMARY_BATCH_SIZE = 5;

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final AgentExecutor agentExecutor;
    private final ConversationSummaryService conversationSummaryService;

    @Transactional
    public MessageResponse send(Long userId, Long conversationId, MessageCreateRequest request) {
        Conversation conversation = conversationRepository.findByIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new AgentFlowException(ErrorCode.CONVERSATION_NOT_FOUND));

        Message userMessage = new Message(conversation, MessageRole.USER, request.content());
        messageRepository.save(userMessage);

        Pageable pageable = PageRequest.of(0, RECENT_MESSAGE_LIMIT, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Message> recentMessages = messageRepository.findByConversationId(conversationId, pageable);
        Collections.reverse(recentMessages);

        List<Message> pendingMessages = resolvePendingMessages(conversation, recentMessages);

        List<Message> contextMessages = new ArrayList<>(pendingMessages);
        contextMessages.addAll(recentMessages);

        List<org.springframework.ai.chat.messages.Message> chatMessages =
                contextMessages.stream()
                        .map(this::convertMessage)
                        .toList();

        String answer = agentExecutor.execute(conversation.getAgent(), chatMessages, conversation.getSummary(), userId);

        Message assistantMessage = new Message(conversation, MessageRole.ASSISTANT, answer);
        Message savedMessage = messageRepository.save(assistantMessage);
        return new MessageResponse(savedMessage.getId(), savedMessage.getRole(), savedMessage.getContent());
    }

    /**
     * 윈도우 밖으로 밀려났지만 아직 요약되지 않은 메시지를 조회한다.
     * - 개수가 임계값(SUMMARY_BATCH_SIZE) 이상이면 요약 LLM을 호출해 summary에 반영하고, 빈 리스트를 반환한다.
     * - 임계값 미만이면 요약 호출 없이 그대로 반환한다 (호출부에서 원문 그대로 컨텍스트에 포함시켜 정보 유실을 막는다).
     * - 요약 호출이 실패하면 메시지 목록을 그대로 반환해 이번 턴에는 원문으로라도 컨텍스트에 포함되게 한다.
     */
    private List<Message> resolvePendingMessages(Conversation conversation, List<Message> recentMessages) {
        if (recentMessages.isEmpty()) {
            return Collections.emptyList();
        }

        Long windowStartId = recentMessages.get(0).getId();
        Long lastSummarizedId = conversation.getLastSummarizedMessageId();
        long lowerBound = (lastSummarizedId != null) ? lastSummarizedId : 0L;

        List<Message> pendingMessages = messageRepository
                .findByConversationIdAndIdGreaterThanAndIdLessThanOrderByIdAsc(
                        conversation.getId(), lowerBound, windowStartId);

        if (pendingMessages.isEmpty() || pendingMessages.size() < SUMMARY_BATCH_SIZE) {
            return pendingMessages;
        }

        try {
            String newSummary = conversationSummaryService.summarize(conversation.getSummary(), pendingMessages);
            Long newLastSummarizedId = pendingMessages.get(pendingMessages.size() - 1).getId();
            conversation.updateSummary(newSummary, newLastSummarizedId);
            return Collections.emptyList();
        } catch (Exception e) {
            log.warn("대화 요약 생성 실패. conversationId={}, 이번 턴은 원문으로 대체합니다.", conversation.getId(), e);
            return pendingMessages;
        }
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