package com.example.ai.infrastructure.embedding;

import org.springframework.stereotype.Service;

import com.example.ai.domain.model.EmbeddingModelType;

/*
 * 測試用的
 * */
@Service
public class DummyEmbeddingService implements EmbeddingService{
	
	@Override
	public EmbeddingModelType getModelType() {
		return EmbeddingModelType.DUMMY;
	}
	
	@Override
	public float[] getEmbedding(String text) {
		return new float[768];
	}

}
