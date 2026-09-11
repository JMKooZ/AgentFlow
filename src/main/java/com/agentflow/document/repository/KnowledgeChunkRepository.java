package com.agentflow.document.repository;
import com.agentflow.document.entity.KnowledgeChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface KnowledgeChunkRepository extends JpaRepository<KnowledgeChunk, Long> {
    List<KnowledgeChunk> findAllByDocumentUserId(Long userId);
}
