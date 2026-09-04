package com.agentflow.user.controller;

import com.agentflow.common.CommonFunction;
import com.agentflow.common.response.ApiResponse;
import com.agentflow.user.dto.UserCreateRequest;
import com.agentflow.user.dto.UserResponse;
import com.agentflow.user.dto.UserUpdateRequest;
import com.agentflow.user.entity.User;
import com.agentflow.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
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
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(Authentication authentication) {
        Long userId = CommonFunction.getUserId(authentication);

        User user = userService.getUser(userId);
        return ResponseEntity.ok(ApiResponse.success(UserResponse.from(user)));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyInfo(Authentication authentication, @Valid @RequestBody UserUpdateRequest request) {
        Long userId = CommonFunction.getUserId(authentication);

        User user = userService.updateUser(userId, request.name(), request.password());

        return ResponseEntity.ok(ApiResponse.success(UserResponse.from(user)));
    }
}