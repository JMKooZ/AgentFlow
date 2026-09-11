CREATE TABLE agent_execution_logs
(
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    user_id         BIGINT      NOT NULL,
    agent_id        BIGINT      NOT NULL,
    conversation_id BIGINT,
    status          VARCHAR(20) NOT NULL,
    input_message   TEXT        NOT NULL,
    output_message  TEXT,
    error_message   TEXT,
    started_at      DATETIME    NOT NULL,
    completed_at    DATETIME,

    CONSTRAINT pk_agent_execution_logs PRIMARY KEY (id),
    CONSTRAINT fk_execution_logs_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_execution_logs_agent FOREIGN KEY (agent_id) REFERENCES agents (id),
    CONSTRAINT fk_execution_logs_conversation FOREIGN KEY (conversation_id) REFERENCES conversations (id)
);
