package com.agentflow.document.service;

import com.agentflow.document.entity.KnowledgeChunk;
import com.agentflow.document.entity.KnowledgeDocument;
import com.agentflow.document.repository.KnowledgeChunkRepository;
import com.agentflow.document.repository.KnowledgeDocumentRepository;
import com.agentflow.user.entity.User;
import com.agentflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentRagService {
    private static final int CHUNK_SIZE = 1_000;
    private static final int MAX_CONTEXT_CHUNKS = 5;
    private final UserRepository userRepository;
    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeChunkRepository chunkRepository;

    @Transactional
    public Long upload(Long userId, MultipartFile file) throws IOException {
        if (file.isEmpty() || file.getOriginalFilename() == null) throw new IllegalArgumentException("비어 있는 파일은 업로드할 수 없습니다.");
        String name = file.getOriginalFilename();
        if (!name.endsWith(".txt") && !name.endsWith(".md")) throw new IllegalArgumentException("현재는 .txt, .md 문서만 지원합니다.");
        User user = userRepository.getReferenceById(userId);
        KnowledgeDocument document = documentRepository.save(new KnowledgeDocument(user, name));
        String content = new String(file.getBytes(), StandardCharsets.UTF_8).trim();
        for (int index = 0; index * CHUNK_SIZE < content.length(); index++) {
            int start = index * CHUNK_SIZE;
            chunkRepository.save(new KnowledgeChunk(document, index, content.substring(start, Math.min(start + CHUNK_SIZE, content.length()))));
        }
        return document.getId();
    }

    @Transactional(readOnly = true)
    public String contextFor(Long userId, String question) {
        List<String> terms = Arrays.stream(question.toLowerCase().split("\\s+"))
                .filter(term -> term.length() > 1).toList();
        return chunkRepository.findAllByDocumentUserId(userId).stream()
                .sorted(Comparator.comparingInt((KnowledgeChunk chunk) -> score(chunk.getContent(), terms)).reversed())
                .filter(chunk -> score(chunk.getContent(), terms) > 0).limit(MAX_CONTEXT_CHUNKS)
                .map(chunk -> "[" + chunk.getDocument().getFileName() + "]\n" + chunk.getContent())
                .reduce("", (left, right) -> left.isBlank() ? right : left + "\n\n" + right);
    }

    private int score(String content, List<String> terms) {
        String lower = content.toLowerCase();
        return (int) terms.stream().filter(lower::contains).count();
    }
}
