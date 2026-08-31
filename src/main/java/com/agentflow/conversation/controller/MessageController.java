package com.agentflow.conversation.controller;

import com.agentflow.common.CommonFunction;
import com.agentflow.common.response.ApiResponse;
import com.agentflow.conversation.dto.MessageCreateRequest;
import com.agentflow.conversation.dto.MessageResponse;
import com.agentflow.conversation.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conversations/{conversationId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> send(Authentication authentication, @PathVariable Long conversationId, @Valid @RequestBody MessageCreateRequest request) {
        Long userId = CommonFunction.getUserId(authentication);

        MessageResponse response =
                messageService.send(
                        userId,
                        conversationId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }
}