package com.agentflow.tool.file;

import com.agentflow.file.entity.AgentFile;
import com.agentflow.file.repository.AgentFileRepository;
import com.agentflow.user.entity.User;
import com.agentflow.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
public class FileWriteTool {

    private static final int MAX_CONTENT_LENGTH = 20_000;
    private static final int MAX_FILE_NAME_LENGTH = 50;

    private final Long userId;
    private final String baseStorageDir;
    private final AgentFileRepository agentFileRepository;
    private final UserRepository userRepository;

    public FileWriteTool(Long userId, String baseStorageDir, AgentFileRepository agentFileRepository, UserRepository userRepository) {
        this.userId = userId;
        this.baseStorageDir = baseStorageDir;
        this.agentFileRepository = agentFileRepository;
        this.userRepository = userRepository;
    }

    @Tool(description = "텍스트 내용을 파일로 생성해서 저장한다. 사용자가 '이거 파일로 만들어줘', " +
            "'텍스트 파일로 저장해줘', '메모 저장해줘' 등을 요청할 때 사용한다. 저장 후 파일 ID를 반환한다.")
    public String saveTextFile(
            @ToolParam(description = "저장할 파일 이름 (확장자 없이, 예: '회의록', 'todo')") String fileName,
            @ToolParam(description = "파일에 저장할 텍스트 내용") String content) {

        if (content == null || content.isBlank()) {
            return "저장할 내용이 비어있어 파일을 만들지 않았습니다.";
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            return "내용이 너무 깁니다 (최대 " + MAX_CONTENT_LENGTH + "자). 내용을 줄여서 다시 시도해주세요.";
        }

        String safeName = sanitizeFileName(fileName);

        try {
            Path userDir = Path.of(baseStorageDir, String.valueOf(userId));
            Files.createDirectories(userDir);

            String storedFileName = System.currentTimeMillis() + "_" + safeName;
            Path filePath = userDir.resolve(storedFileName).normalize();

            if (!filePath.startsWith(userDir.normalize())) {
                log.warn("[Tool 호출] saveTextFile 경로 이탈 시도 차단. userId={}, fileName={}", userId, fileName);
                return "파일 이름이 올바르지 않아 저장에 실패했습니다.";
            }

            Files.writeString(filePath, content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            User user = userRepository.getReferenceById(userId);
            AgentFile agentFile = new AgentFile(user, safeName, filePath.toString(), Files.size(filePath));
            agentFileRepository.save(agentFile);

            log.info("[Tool 호출] saveTextFile(userId={}, fileName={}) -> fileId={}", userId, safeName, agentFile.getId());

            return "파일이 저장되었습니다. (파일 ID: " + agentFile.getId() + ", 파일명: " + safeName + ") " +
                    "GET /files/" + agentFile.getId() + "/download 로 다운로드할 수 있습니다.";
        } catch (IOException e) {
            log.error("[Tool 호출] saveTextFile 실패. userId={}", userId, e);
            return "파일 저장 중 오류가 발생했습니다.";
        }
    }

    private String sanitizeFileName(String rawName) {
        String base = (rawName == null) ? "untitled" : rawName;

        String cleaned = base.replaceAll("[^a-zA-Z0-9가-힣_\\-]", "").trim();

        if (cleaned.isBlank()) {
            cleaned = "untitled";
        }
        if (cleaned.length() > MAX_FILE_NAME_LENGTH) {
            cleaned = cleaned.substring(0, MAX_FILE_NAME_LENGTH);
        }

        return cleaned + ".txt";
    }
}