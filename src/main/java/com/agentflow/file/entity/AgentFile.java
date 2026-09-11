package com.agentflow.file.entity;

import com.agentflow.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "agent_files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 500)
    private String storagePath;

    @Column(nullable = false)
    private long sizeBytes;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public AgentFile(User user, String fileName, String storagePath, long sizeBytes) {
        this.user = user;
        this.fileName = fileName;
        this.storagePath = storagePath;
        this.sizeBytes = sizeBytes;
        this.createdAt = LocalDateTime.now();
    }
}