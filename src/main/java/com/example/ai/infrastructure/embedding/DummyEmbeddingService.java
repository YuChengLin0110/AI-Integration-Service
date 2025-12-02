package com.example.ai.infrastructure.embedding;

import org.springframework.stereotype.Service;

@Service
public class DummyEmbeddingService implements EmbeddingService{
	
	@Override
	public float[] getEmbedding(String text) {
		return new float[768];
	}

}
