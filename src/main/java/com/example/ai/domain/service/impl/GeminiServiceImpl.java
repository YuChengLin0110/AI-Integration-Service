package com.example.ai.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.ai.core.prompt.PromptBuilder;
import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;
import com.example.ai.domain.model.PromptRequest;
import com.example.ai.domain.service.AiService;
import com.example.ai.infrastructure.llm.AiModelClient;

@Service
public class GeminiServiceImpl implements AiService{
	
	private final AiModelClient client;
	
	@Autowired
	public GeminiServiceImpl(@Qualifier("GEMINI") AiModelClient client) {
		this.client = client;
	}

	@Override
	public AiModelType getModelType() {
		return AiModelType.GEMINI;
	}

	@Override
	public AiResponse complete(PromptRequest request) {
		// 使用 PromptBuilder 建立 prompt
        // 模板: "回答以下問題：{{content}}"
        // 透過 addValues 將 request 的 prompt 填入模板
		String prompt = new PromptBuilder()
				.setTemplate("回答以下問題：{{content}}")
				.addValues("content", request.getPrompt())
				.build();
		
		String result = client.completion(prompt);
		
		return new AiResponse(result);
	}

	
	

}
