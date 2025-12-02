package com.example.ai.infrastructure.llm;

import com.example.ai.domain.model.AiModelType;

public interface AiModelClient {
	AiModelType getModelType();
	String completion(String prompt);
}
