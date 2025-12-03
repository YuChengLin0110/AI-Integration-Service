package com.example.ai.infrastructure.llm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ai.domain.model.AiModelType;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Component("GEMINI")
public class GeminiModelClient implements AiModelClient{
	
	private final Client client;
	private final String MODEL_NAME = "gemini-2.5-flash";
	
	@Autowired
	public GeminiModelClient(Client client) {
		this.client = client;
	}

	@Override
	public AiModelType getModelType() {
		return AiModelType.GEMINI;
	}

	@Override
	public String completion(String prompt) {
		GenerateContentResponse resp = client.models.generateContent(MODEL_NAME, prompt, null);
		
		String text = resp.text();
		
		if(text != null) {
			return text;
		}else {
			return "";
		}
	}
	
}
