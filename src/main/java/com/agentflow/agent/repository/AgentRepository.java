package com.agentflow.agent.repository;

import com.agentflow.agent.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    List<Agent> findAllByUserId(Long userId);

    Optional<Agent> findByIdAndUserId(Long id, Long userId);
}
