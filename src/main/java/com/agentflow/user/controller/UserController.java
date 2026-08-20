package com.agentflow.user.controller;

import com.agentflow.user.dto.UserCreateRequest;
import com.agentflow.user.dto.UserResponse;
import com.agentflow.user.entity.User;
import com.agentflow.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request
    ) {
        System.out.println("controller 호출");
        User user = userService.createUser(
                request.email(),
                request.password(),
                request.name()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserResponse.from(user));
    }
}