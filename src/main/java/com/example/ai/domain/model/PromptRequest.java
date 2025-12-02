package com.example.ai.domain.model;

public class PromptRequest {
	private String prompt;
	
	public PromptRequest() {}
	
	public PromptRequest(String prompt) {
		this.prompt = prompt;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	
	
}
