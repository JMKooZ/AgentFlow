package com.agentflow.execution.service;

import com.agentflow.agent.entity.Agent;
import com.agentflow.conversation.entity.Conversation;
import com.agentflow.execution.entity.AgentExecutionLog;
import com.agentflow.execution.repository.AgentExecutionLogRepository;
import com.agentflow.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExecutionLogService {

    private final AgentExecutionLogRepository executionLogRepository;

    @Transactional
    public Long start(User user, Agent agent, Conversation conversation, String inputMessage) {
        AgentExecutionLog executionLog = new AgentExecutionLog(user, agent, conversation, inputMessage);
        return executionLogRepository.save(executionLog).getId();
    }

    @Transactional
    public void succeed(Long executionLogId, String outputMessage) {
        executionLogRepository.findById(executionLogId)
                .ifPresent(log -> log.succeed(outputMessage));
    }

    @Transactional
    public void fail(Long executionLogId, Throwable throwable) {
        String message = throwable.getMessage();
        String errorMessage = (message == null || message.isBlank())
                ? throwable.getClass().getSimpleName()
                : message;

        executionLogRepository.findById(executionLogId)
                .ifPresent(log -> log.fail(errorMessage));
    }
}
