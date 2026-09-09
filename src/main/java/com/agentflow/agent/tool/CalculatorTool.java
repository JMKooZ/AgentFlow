package com.agentflow.agent.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CalculatorTool {

    @Tool(description = "두 숫자를 더한다.")
    public double add(@ToolParam(description = "첫 번째 숫자") double a, @ToolParam(description = "두 번째 숫자") double b) {
        double result = a + b;
        log.info("[Tool 호출] add({}, {}) -> {}", a, b, result);
        return result;
    }

    @Tool(description = "첫 번째 숫자에서 두 번째 숫자를 뺀다.")
    public double subtract(@ToolParam(description = "첫 번째 숫자") double a, @ToolParam(description = "두 번째 숫자") double b) {
        double result = a - b;
        log.info("[Tool 호출] subtract({}, {}) -> {}", a, b, result);
        return result;
    }

    @Tool(description = "두 숫자를 곱한다.")
    public double multiply(@ToolParam(description = "첫 번째 숫자") double a, @ToolParam(description = "두 번째 숫자") double b) {
        double result = a * b;
        log.info("[Tool 호출] multiply({}, {}) -> {}", a, b, result);
        return result;
    }

    @Tool(description = "첫 번째 숫자를 두 번째 숫자로 나눈다. 0으로 나누면 오류 메시지를 반환한다.")
    public String divide(@ToolParam(description = "나눠지는 숫자") double a, @ToolParam(description = "나누는 숫자") double b) {
        if (b == 0) {
            log.warn("[Tool 호출] divide({}, {}) -> 0으로 나누기 시도", a, b);
            return "0으로 나눌 수 없습니다.";
        }
        double result = a / b;
        log.info("[Tool 호출] divide({}, {}) -> {}", a, b, result);
        return String.valueOf(result);
    }
}