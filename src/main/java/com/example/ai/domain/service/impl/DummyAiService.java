package com.example.ai.domain.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;
import com.example.ai.domain.service.AiService;
import com.example.ai.formatter.PromptFormatter;
import com.example.ai.infrastructure.llm.AiModelClient;

@Service
public class DummyAiService implements AiService {
	
	private final AiModelClient aiModelClient;
	private final PromptFormatter promptFormatter;
	
	public DummyAiService (@Qualifier("DUMMY") AiModelClient aiModelClient, PromptFormatter promptFormatter) {
		this.aiModelClient = aiModelClient;
		this.promptFormatter = promptFormatter;
	}

	@Override
	public AiResponse complete(String question) {
		String prompt = promptFormatter.formatSimplePrompt(question);
		String result = aiModelClient.completion(question);
		return new AiResponse(result);
	}

	@Override
	public AiModelType getModelType() {
		return AiModelType.DUMMY;
	}

}
