package com.agentflow.conversation.repository;

import com.agentflow.conversation.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByConversationIdOrderByCreatedAtAsc(Long conversationId);

    List<Message> findByConversationId(Long conversationId, Pageable pageable);

    long countByConversationId(Long conversationId);

    List<Message> findByConversationIdAndIdGreaterThanAndIdLessThanOrderByIdAsc(Long conversationId, Long idGreaterThan, Long idLessThan);
}