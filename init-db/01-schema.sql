-- 先啟用 PgVector extension 才能使用 vector
CREATE EXTENSION IF NOT EXISTS vector;

-- 建立向量表
CREATE TABLE IF NOT EXISTS documents (
    id VARCHAR(255) NOT NULL,
	doc_id VARCHAR(255),
    chunk TEXT NOT NULL,
    embedding vector(1536), -- PgVector 向量欄位 對應 OpenAI embedding 長度
    PRIMARY KEY (id)
);

-- 建立 IVFFlat index (加快 topK 查詢)
CREATE INDEX idx_documents_embedding ON documents USING ivfflat (embedding vector_l2_ops)
WITH (lists = 100); 