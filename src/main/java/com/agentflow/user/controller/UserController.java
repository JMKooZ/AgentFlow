package com.agentflow.user.controller;

import com.agentflow.common.response.ApiResponse;
import com.agentflow.user.dto.UserCreateRequest;
import com.agentflow.user.dto.UserResponse;
import com.agentflow.user.entity.User;
import com.agentflow.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.createUser(
                request.email(),
                request.password(),
                request.name()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(UserResponse.from(user)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getMyInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        return ResponseEntity.ok(ApiResponse.success(Map.of("userId", userId)));
    }
}