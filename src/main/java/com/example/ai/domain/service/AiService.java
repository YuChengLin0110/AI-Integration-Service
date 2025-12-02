package com.example.ai.domain.service;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;
import com.example.ai.domain.model.PromptRequest;

public interface AiService {
	AiModelType getModelType();
	AiResponse complete(PromptRequest request);
}
