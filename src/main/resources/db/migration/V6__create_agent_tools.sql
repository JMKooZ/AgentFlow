CREATE TABLE agent_tools
(
    agent_id BIGINT      NOT NULL,
    tool     VARCHAR(30) NOT NULL,

    CONSTRAINT fk_agent_tools_agent
        FOREIGN KEY (agent_id)
            REFERENCES agents (id)
);