CREATE TABLE knowledge_documents
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    user_id    BIGINT       NOT NULL,
    file_name  VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL,
    CONSTRAINT pk_knowledge_documents PRIMARY KEY (id),
    CONSTRAINT fk_knowledge_documents_user FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE TABLE knowledge_chunks
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    document_id BIGINT NOT NULL,
    chunk_index INT    NOT NULL,
    content     TEXT   NOT NULL,
    CONSTRAINT pk_knowledge_chunks PRIMARY KEY (id),
    CONSTRAINT fk_knowledge_chunks_document FOREIGN KEY (document_id) REFERENCES knowledge_documents (id)
);
