package com.agentflow.execution.entity;

import com.agentflow.agent.entity.Agent;
import com.agentflow.conversation.entity.Conversation;
import com.agentflow.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "agent_execution_logs")
public class AgentExecutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExecutionStatus status;

    @Column(name = "input_message", nullable = false, columnDefinition = "TEXT")
    private String inputMessage;

    @Column(name = "output_message", columnDefinition = "TEXT")
    private String outputMessage;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public AgentExecutionLog(User user, Agent agent, Conversation conversation, String inputMessage) {
        this.user = user;
        this.agent = agent;
        this.conversation = conversation;
        this.status = ExecutionStatus.RUNNING;
        this.inputMessage = inputMessage;
        this.startedAt = LocalDateTime.now();
    }

    public void succeed(String outputMessage) {
        this.status = ExecutionStatus.SUCCEEDED;
        this.outputMessage = outputMessage;
        this.completedAt = LocalDateTime.now();
    }

    public void fail(String errorMessage) {
        this.status = ExecutionStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = LocalDateTime.now();
    }
}
