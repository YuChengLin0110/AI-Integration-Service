package com.example.ai.domain.service;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;

public interface RagService {
	AiResponse query(String question, AiModelType model);
}
