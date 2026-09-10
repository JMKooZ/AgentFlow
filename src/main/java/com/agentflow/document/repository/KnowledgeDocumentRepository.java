package com.agentflow.document.repository;
import com.agentflow.document.entity.KnowledgeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocument, Long> { }
