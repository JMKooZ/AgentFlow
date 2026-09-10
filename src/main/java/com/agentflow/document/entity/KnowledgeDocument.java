package com.agentflow.document.entity;

import com.agentflow.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "knowledge_documents")
public class KnowledgeDocument {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false, length = 255) private String fileName;
    @Column(nullable = false) private LocalDateTime createdAt;

    public KnowledgeDocument(User user, String fileName) {
        this.user = user;
        this.fileName = fileName;
        this.createdAt = LocalDateTime.now();
    }
}
