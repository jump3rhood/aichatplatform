-- Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

-- Create vector store table (Spring AI will use this)
CREATE TABLE IF NOT EXISTS vector_store (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content TEXT NOT NULL,
    metadata JSONB,
    embedding vector(1536)  -- OpenAI embeddings are 1536 dimensions
);

-- Create index for fast similarity search
CREATE INDEX IF NOT EXISTS vector_store_embedding_idx
ON vector_store USING ivfflat (embedding vector_cosine_ops)
WITH (lists = 100);