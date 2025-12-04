package com.example.ai.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;
import com.example.ai.domain.service.AiService;
import com.example.ai.formatter.PromptFormatter;
import com.example.ai.infrastructure.llm.AiModelClient;

@Service
@ConditionalOnProperty(name = "openai.enabled", havingValue = "true", matchIfMissing = false)
public class OpenAiServiceImpl implements AiService{
	
	private final AiModelClient client;
	private final PromptFormatter promptFormatter;
	
	@Autowired
	public OpenAiServiceImpl(@Qualifier("OPENAI") AiModelClient client, PromptFormatter promptFormatter) {
        this.client = client;
        this.promptFormatter = promptFormatter;
    }
	
	@Override
	public AiModelType getModelType() {
		return AiModelType.OPENAI;
	}

	@Override
	public AiResponse complete(String question) {
		
		// 使用 PromptBuilder 建立 prompt
        // 模板: "回答以下問題：{{content}}"
        // 透過 addValues 將 request 的 prompt 填入模板
		String prompt = promptFormatter.formatSimplePrompt(question);
		
		String result = client.completion(prompt);
		
		return new AiResponse(result);
	}

}
