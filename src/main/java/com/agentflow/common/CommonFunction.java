package com.agentflow.common;

import com.agentflow.common.exception.AgentFlowException;
import com.agentflow.common.exception.ErrorCode;
import org.springframework.security.core.Authentication;

public class CommonFunction {

    public static Long getUserId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Long userId)) {
            throw new AgentFlowException(ErrorCode.UNAUTHORIZED);
        }
        return userId;
    }
}