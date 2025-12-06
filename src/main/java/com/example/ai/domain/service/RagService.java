package com.example.ai.domain.service;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;
import com.example.ai.domain.model.EmbeddingModelType;
import com.example.ai.domain.model.VectorStoreType;

public interface RagService {
	AiResponse query(String question, AiModelType aiModel, EmbeddingModelType embeddingModel, VectorStoreType storeType);
}
