package com.agentflow.document.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "knowledge_chunks")
public class KnowledgeChunk {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "document_id", nullable = false)
    private KnowledgeDocument document;
    @Column(name = "chunk_index", nullable = false) private int chunkIndex;
    @Column(nullable = false, columnDefinition = "TEXT") private String content;

    public KnowledgeChunk(KnowledgeDocument document, int chunkIndex, String content) {
        this.document = document;
        this.chunkIndex = chunkIndex;
        this.content = content;
    }
}
