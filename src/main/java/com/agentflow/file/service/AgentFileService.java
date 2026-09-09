package com.agentflow.file.service;

import com.agentflow.common.exception.AgentFlowException;
import com.agentflow.common.exception.ErrorCode;
import com.agentflow.file.dto.AgentFileResponse;
import com.agentflow.file.entity.AgentFile;
import com.agentflow.file.repository.AgentFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgentFileService {

    private final AgentFileRepository agentFileRepository;

    public List<AgentFileResponse> findMyFiles(Long userId) {
        return agentFileRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(AgentFileResponse::from)
                .toList();
    }

    public AgentFile findForDownload(Long userId, Long fileId) {
        AgentFile agentFile = agentFileRepository.findByIdAndUserId(fileId, userId)
                .orElseThrow(() -> new AgentFlowException(ErrorCode.FILE_NOT_FOUND));

        File file = new File(agentFile.getStoragePath());
        if (!file.exists()) {
            throw new AgentFlowException(ErrorCode.FILE_NOT_FOUND);
        }

        return agentFile;
    }

    public Resource toResource(AgentFile agentFile) {
        return new FileSystemResource(agentFile.getStoragePath());
    }
}