package com.agentflow.file.controller;

import com.agentflow.common.CommonFunction;
import com.agentflow.common.response.ApiResponse;
import com.agentflow.file.dto.AgentFileResponse;
import com.agentflow.file.entity.AgentFile;
import com.agentflow.file.service.AgentFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class AgentFileController {

    private final AgentFileService agentFileService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AgentFileResponse>>> findMyFiles(Authentication authentication) {
        Long userId = CommonFunction.getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.success(agentFileService.findMyFiles(userId)));
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> download(Authentication authentication, @PathVariable Long fileId) {
        Long userId = CommonFunction.getUserId(authentication);

        AgentFile agentFile = agentFileService.findForDownload(userId, fileId);
        Resource resource = agentFileService.toResource(agentFile);

        String encodedName = java.net.URLEncoder.encode(agentFile.getFileName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedName)
                .body(resource);
    }
}