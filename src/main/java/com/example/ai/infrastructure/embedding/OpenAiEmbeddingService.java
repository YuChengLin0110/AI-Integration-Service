package com.example.ai.infrastructure.embedding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.openai.client.OpenAIClient;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;

@Service
@ConditionalOnProperty(name = "openai.enabled", havingValue = "true", matchIfMissing = false)
public class OpenAiEmbeddingService implements EmbeddingService{
	
	// OpenAIClient：負責呼叫 OpenAI Embedding API
	private final OpenAIClient client;
	
	@Autowired
	public OpenAiEmbeddingService (OpenAIClient client) {
		this.client = client;
	}
	
	/*
	 * 將文字轉換成向量
	 * */
	@Override
	public float[] getEmbedding(String text) {
		// 建立 Embedding 請求參數
		EmbeddingCreateParams params = EmbeddingCreateParams.builder()
				.model("text-embedding-3-small")
				.input(text)
				.build();
		
		// 呼叫 OpenAI Embedding API
		CreateEmbeddingResponse resp = client.embeddings().create(params);
		
		// 取得第一筆 embedding 資料
		if(resp.data() != null && !resp.data().isEmpty()) {
			var first = resp.data().get(0);
			var vector = first.embedding();
			float[] embedding = new float[vector.size()];
			for(int i = 0 ; i < vector.size(); i++) {
				embedding[i] = vector.get(i);
			}
			
			return embedding;
		}
		
		// 若沒有資料則回傳空向量
		return new float[0];
	}

}
