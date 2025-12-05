package com.example.ai.infrastructure.embedding;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ai.domain.model.EmbeddingModelType;
import com.google.genai.Client;

/*
 * 目前 Google 沒提供
 * */
@Service
public class GeminiEmbeddingService implements EmbeddingService{
	
	private final Client client;
	
	@Autowired
	public GeminiEmbeddingService(Client client) {
		this.client = client;
	}
	
	@Override
	public EmbeddingModelType getModelType() {
		return EmbeddingModelType.GEMINI;
	}

	@Override
	public float[] getEmbedding(String text) {
		// TODO Auto-generated method stub
		return null;
	}

}
