package com.agentflow.agent.repository;

import com.agentflow.agent.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    List<Agent> findAllByUserId(Long userId);

    Optional<Agent> findByIdAndUserId(Long id, Long userId);

    @Query("select a from Agent a left join fetch a.enabledTools where a.id = :agentId")
    Optional<Agent> findByIdWithTools(@Param("agentId") Long agentId);
}
