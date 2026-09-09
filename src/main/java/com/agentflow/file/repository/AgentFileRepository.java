package com.agentflow.file.repository;

import com.agentflow.file.entity.AgentFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgentFileRepository extends JpaRepository<AgentFile, Long> {

    List<AgentFile> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<AgentFile> findByIdAndUserId(Long id, Long userId);
}