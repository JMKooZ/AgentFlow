package com.agentflow.agent.entity;

import com.agentflow.tool.AgentToolType;
import com.agentflow.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "agents")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_agents_user")
    )
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String systemPrompt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "agent_tools",
            joinColumns = @JoinColumn(name = "agent_id")
    )
    @Column(name = "tool")
    @Enumerated(EnumType.STRING)
    private Set<AgentToolType> enabledTools = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Agent(User user, String name, String description, String systemPrompt, Set<AgentToolType> enabledTools) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.systemPrompt = systemPrompt;
        this.enabledTools = enabledTools != null ? new HashSet<>(enabledTools) : new HashSet<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String name, String description, String systemPrompt, Set<AgentToolType> enabledTools) {
        this.name = name;
        this.description = description;
        this.systemPrompt = systemPrompt;
        this.enabledTools = enabledTools != null ? new HashSet<>(enabledTools) : new HashSet<>();
        this.updatedAt = LocalDateTime.now();
    }
}