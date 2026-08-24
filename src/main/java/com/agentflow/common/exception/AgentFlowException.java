package com.agentflow.common.exception;

import lombok.Getter;

@Getter
public class AgentFlowException extends RuntimeException{

    private final ErrorCode errorCode;

    public AgentFlowException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
