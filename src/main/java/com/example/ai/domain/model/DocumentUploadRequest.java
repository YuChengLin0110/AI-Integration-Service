package com.example.ai.domain.model;

import org.springframework.web.multipart.MultipartFile;

public class DocumentUploadRequest {
	private MultipartFile file;
	private EmbeddingModelType embeddingModel = EmbeddingModelType.DUMMY;
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public EmbeddingModelType getEmbeddingModel() {
		return embeddingModel;
	}
	public void setEmbeddingModel(EmbeddingModelType embeddingModel) {
		this.embeddingModel = embeddingModel;
	}
}
