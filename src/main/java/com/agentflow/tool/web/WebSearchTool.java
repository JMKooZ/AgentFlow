package com.agentflow.tool.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebSearchTool {

    private static final int MAX_RESULTS = 3;
    private static final int MAX_CONTENT_LENGTH = 500;

    private final RestClient tavilyRestClient;
    private final String apiKey;

    public WebSearchTool(RestClient tavilyRestClient, @Value("${tavily.api-key}") String apiKey) {
        this.tavilyRestClient = tavilyRestClient;
        this.apiKey = apiKey;
    }

    @Tool(description = "웹에서 최신 정보를 검색한다. 최근 뉴스, 실시간 정보, 학습 데이터에 없을 만한 " +
            "최신 사실관계를 물어볼 때 사용한다. 일반 상식이나 이미 아는 내용에는 사용하지 않는다.")
    public String searchWeb(@ToolParam(description = "검색할 키워드 또는 질문") String query) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("[Tool 호출] searchWeb 실패 - TAVILY_API_KEY 미설정");
            return "웹 검색 기능이 현재 설정되어 있지 않습니다.";
        }

        try {
            Map<String, Object> requestBody = Map.of(
                    "query", query,
                    "max_results", MAX_RESULTS,
                    "include_answer", true
            );

            Map<String, Object> response = tavilyRestClient.post()
                    .uri("/search")
                    .header("Authorization", "Bearer " + apiKey)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            log.info("[Tool 호출] searchWeb(query={}) -> 응답 수신", query);

            return formatResponse(response);
        } catch (Exception e) {
            log.error("[Tool 호출] searchWeb 실패. query={}", query, e);
            return "웹 검색 중 오류가 발생했습니다. 알고 있는 정보로만 답변해주세요.";
        }
    }

    @SuppressWarnings("unchecked")
    private String formatResponse(Map<String, Object> response) {
        if (response == null) {
            return "검색 결과가 없습니다.";
        }

        StringBuilder sb = new StringBuilder();

        Object answer = response.get("answer");
        if (answer instanceof String answerText && !answerText.isBlank()) {
            sb.append("[요약] ").append(answerText).append("\n\n");
        }

        Object rawResults = response.get("results");
        if (rawResults instanceof List<?> results && !results.isEmpty()) {
            String formatted = results.stream()
                    .filter(r -> r instanceof Map)
                    .map(r -> (Map<String, Object>) r)
                    .map(this::formatResult)
                    .collect(Collectors.joining("\n"));
            sb.append(formatted);
        }

        return sb.isEmpty() ? "검색 결과가 없습니다." : sb.toString();
    }

    private String formatResult(Map<String, Object> result) {
        String title = String.valueOf(result.getOrDefault("title", "제목 없음"));
        String url = String.valueOf(result.getOrDefault("url", ""));
        String content = String.valueOf(result.getOrDefault("content", ""));

        if (content.length() > MAX_CONTENT_LENGTH) {
            content = content.substring(0, MAX_CONTENT_LENGTH) + "...";
        }

        return "- %s (%s)\n  %s".formatted(title, url, content);
    }
}