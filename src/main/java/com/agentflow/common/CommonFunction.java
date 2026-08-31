package com.agentflow.common;

import org.springframework.security.core.Authentication;

public class CommonFunction {
    public static Long getUserId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }
}
