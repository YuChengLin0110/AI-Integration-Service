package com.example.ai.domain.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;
import com.example.ai.domain.model.PromptRequest;
import com.example.ai.domain.service.AiService;
import com.example.ai.infrastructure.llm.AiModelClient;

@Service
public class DummyAiService implements AiService {
	
	private final AiModelClient aiModelClient;
	
	public DummyAiService (@Qualifier("DUMMY") AiModelClient aiModelClient) {
		this.aiModelClient = aiModelClient;
	}

	@Override
	public AiResponse complete(PromptRequest request) {
		String result = aiModelClient.completion(request.getPrompt());
		return new AiResponse(result);
	}

	@Override
	public AiModelType getModelType() {
		return AiModelType.DUMMY;
	}

}
