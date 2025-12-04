package com.example.ai.domain.service;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;

public interface AiService {
	AiModelType getModelType();
	AiResponse complete(String question);
}
