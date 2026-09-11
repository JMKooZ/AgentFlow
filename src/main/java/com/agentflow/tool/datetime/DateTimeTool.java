package com.agentflow.tool.datetime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class DateTimeTool {

    @Tool(description = "현재 날짜와 시간을 조회한다. 사용자가 오늘 날짜, 현재 시각, 지금 몇 시인지 등을 물어볼 때 사용한다.")
    public String getCurrentDateTime() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss (E)"));
        log.info("[Tool 호출] getCurrentDateTime -> {}", now);
        return now;
    }
}