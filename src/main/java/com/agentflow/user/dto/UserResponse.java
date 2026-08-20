package com.agentflow.user.dto;

import com.agentflow.user.entity.User;

public record UserResponse(
        Long id,
        String email,
        String name,
        String role,
        String status
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                user.getStatus().name()
        );
    }
}