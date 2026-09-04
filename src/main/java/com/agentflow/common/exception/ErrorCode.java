package com.agentflow.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON-001", "잘못된 요청입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-002", "서버 내부 오류가 발생했습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-001", "인증이 필요합니다."),

    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH-002", "접근 권한이 없습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "사용자를 찾을 수 없습니다."),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER-002", "이미 사용 중인 이메일입니다."),

    AGENT_NOT_FOUND(HttpStatus.NOT_FOUND, "AGENT-001", "AGENT를 찾을 수 없습니다."),

    AGENT_HAS_CONVERSATIONS(HttpStatus.CONFLICT, "AGENT-002", "연결된 대화가 있어 Agent를 삭제할 수 없습니다."),

    CONVERSATION_NOT_FOUND(HttpStatus.NOT_FOUND, "CONVERSATION-001", "CONVERSATION을 찾을 수 없습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}