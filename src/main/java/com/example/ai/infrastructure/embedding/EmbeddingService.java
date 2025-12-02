package com.example.ai.infrastructure.embedding;

public interface EmbeddingService {
	float[] getEmbedding(String text);
}
