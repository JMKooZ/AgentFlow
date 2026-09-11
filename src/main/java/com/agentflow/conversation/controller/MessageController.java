package com.agentflow.conversation.controller;

import com.agentflow.common.CommonFunction;
import com.agentflow.common.response.ApiResponse;
import com.agentflow.conversation.dto.MessageCreateRequest;
import com.agentflow.conversation.dto.MessageResponse;
import com.agentflow.conversation.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/conversations/{conversationId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MessageResponse>>> findAll(Authentication authentication, @PathVariable Long conversationId) {
        Long userId = CommonFunction.getUserId(authentication);
        List<MessageResponse> responses = messageService.findAllByConversation(userId, conversationId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

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

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(
            Authentication authentication,
            @PathVariable Long conversationId,
            @Valid @RequestBody MessageCreateRequest request) {

        Long userId = CommonFunction.getUserId(authentication);

        return messageService.stream(userId, conversationId, request)
                .map(chunk -> ServerSentEvent.builder(chunk).event("chunk").build())
                .concatWithValues(ServerSentEvent.<String>builder().event("complete").build());
    }
}
