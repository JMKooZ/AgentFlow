package com.agentflow.agent.controller;

import com.agentflow.agent.dto.AgentCreateRequest;
import com.agentflow.agent.dto.AgentResponse;
import com.agentflow.agent.dto.AgentUpdateRequest;
import com.agentflow.agent.service.AgentService;
import com.agentflow.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agents")
public class AgentController {
    private final AgentService agentService;

    @PostMapping
    public ResponseEntity<ApiResponse<AgentResponse>> create(Authentication authentication, @Valid @RequestBody AgentCreateRequest request) {
        Long userId = getUserId(authentication);

        AgentResponse response = agentService.create(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AgentResponse>>> findMyAgents(Authentication authentication) {
        Long userId = getUserId(authentication);

        List<AgentResponse> responses = agentService.findMyAgents(userId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{agentId}")
    public ResponseEntity<ApiResponse<AgentResponse>> find(Authentication authentication, @PathVariable Long agentId) {
        Long userId = getUserId(authentication);

        AgentResponse response = agentService.find(userId, agentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{agentId}")
    public ResponseEntity<ApiResponse<AgentResponse>> update(Authentication authentication, @PathVariable Long agentId, @Valid @RequestBody AgentUpdateRequest request) {
        Long userId = getUserId(authentication);

        AgentResponse response = agentService.update(userId, agentId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{agentId}")
    public ResponseEntity<ApiResponse<AgentResponse>> delete(Authentication authentication, @PathVariable Long agentId) {
        Long userId = getUserId(authentication);

        agentService.delete(userId, agentId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private Long getUserId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }
}
