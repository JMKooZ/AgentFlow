package com.agentflow.conversation.controller;

import com.agentflow.common.CommonFunction;
import com.agentflow.common.response.ApiResponse;
import com.agentflow.conversation.dto.ConversationCreateRequest;
import com.agentflow.conversation.dto.ConversationResponse;
import com.agentflow.conversation.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/agents/{agentId}/conversations")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ConversationResponse>> create(Authentication authentication, @PathVariable Long agentId, @Valid @RequestBody ConversationCreateRequest request) {
        Long userId = CommonFunction.getUserId(authentication);
        ConversationResponse response = conversationService.create(userId, agentId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<ConversationResponse>> delete(Authentication authentication, @PathVariable Long conversationId) {
        Long userId = CommonFunction.getUserId(authentication);
        conversationService.delete(userId, conversationId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
