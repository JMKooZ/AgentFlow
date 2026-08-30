CREATE TABLE agents
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    created_at  DATETIME     NOT NULL,
    updated_at  DATETIME     NOT NULL,

    CONSTRAINT pk_agents PRIMARY KEY (id),

    CONSTRAINT fk_agents_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);