CREATE TABLE conversations
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    user_id    BIGINT       NOT NULL,
    agent_id   BIGINT       NOT NULL,
    title      VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,

    CONSTRAINT pk_conversations PRIMARY KEY (id),

    CONSTRAINT fk_conversations_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    CONSTRAINT fk_conversations_agent
        FOREIGN KEY (agent_id)
            REFERENCES agents (id)
);