CREATE TABLE messages
(
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    conversation_id BIGINT      NOT NULL,
    role            VARCHAR(20) NOT NULL,
    content         TEXT        NOT NULL,
    created_at      DATETIME    NOT NULL,

    CONSTRAINT pk_messages PRIMARY KEY (id),

    CONSTRAINT fk_messages_conversation
        FOREIGN KEY (conversation_id)
            REFERENCES conversations (id)
);