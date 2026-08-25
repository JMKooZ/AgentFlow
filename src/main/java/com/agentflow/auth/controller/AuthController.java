package com.agentflow.auth.controller;

import com.agentflow.auth.dto.LoginRequest;
import com.agentflow.auth.dto.LoginResponse;
import com.agentflow.auth.dto.RefreshTokenRequest;
import com.agentflow.auth.service.AuthService;
import com.agentflow.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        String accessToken = authService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(accessToken));
    }
}
