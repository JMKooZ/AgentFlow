package com.agentflow.conversation.repository;

import com.agentflow.conversation.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findAllByUserId(Long userId);

    Optional<Conversation> findByIdAndUserId(Long id, Long userId);

    List<Conversation> findAllByAgentIdAndUserId(Long agentId, Long userId);

    boolean existsByAgentId(Long agentId);
}