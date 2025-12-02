package com.example.ai.infrastructure.llm;

import org.springframework.stereotype.Component;

import com.example.ai.domain.model.AiModelType;

@Component("DUMMY")
public class DummyAiModelClient implements AiModelClient{

	@Override
	public AiModelType getModelType() {
		return AiModelType.DUMMY;
	}
	
	@Override
	public String completion(String prompt) {
		return "Dummy 回應: " + prompt;
	}

}
