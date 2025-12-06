package com.example.ai.domain.model;

public class RagQueryRequest {
	private String question;
    private AiModelType aiModel = AiModelType.DUMMY;
    private EmbeddingModelType embeddingModel = EmbeddingModelType.DUMMY;
    
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public AiModelType getAiModel() {
		return aiModel;
	}
	public void setAiModel(AiModelType aiModel) {
		this.aiModel = aiModel;
	}
	public EmbeddingModelType getEmbeddingModel() {
		return embeddingModel;
	}
	public void setEmbeddingModel(EmbeddingModelType embeddingModel) {
		this.embeddingModel = embeddingModel;
	}
}
