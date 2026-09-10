package com.agentflow.document.controller;

import com.agentflow.common.CommonFunction;
import com.agentflow.common.response.ApiResponse;
import com.agentflow.document.service.DocumentRagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/knowledge-documents")
public class KnowledgeDocumentController {
    private final DocumentRagService documentRagService;
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Long>> upload(Authentication authentication, @RequestPart("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(ApiResponse.success(documentRagService.upload(CommonFunction.getUserId(authentication), file)));
    }
}
