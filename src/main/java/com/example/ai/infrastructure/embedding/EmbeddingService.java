package com.example.ai.infrastructure.embedding;

import com.example.ai.domain.model.EmbeddingModelType;

public interface EmbeddingService {
	EmbeddingModelType getModelType();
	float[] getEmbedding(String text);
}
