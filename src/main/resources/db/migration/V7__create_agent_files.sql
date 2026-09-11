CREATE TABLE agent_files
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    user_id      BIGINT       NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    size_bytes   BIGINT       NOT NULL,
    created_at   DATETIME     NOT NULL,

    CONSTRAINT pk_agent_files PRIMARY KEY (id),

    CONSTRAINT fk_agent_files_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);