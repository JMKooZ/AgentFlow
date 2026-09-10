package com.agentflow.execution.repository;

import com.agentflow.execution.entity.AgentExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentExecutionLogRepository extends JpaRepository<AgentExecutionLog, Long> {
}
