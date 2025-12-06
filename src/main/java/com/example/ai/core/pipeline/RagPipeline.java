package com.example.ai.core.pipeline;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;
import com.example.ai.domain.model.EmbeddingModelType;
import com.example.ai.domain.model.VectorStoreType;
import com.example.ai.factory.AiModelClientFactory;
import com.example.ai.factory.EmbeddingModeFactory;
import com.example.ai.factory.VectorStoreFactory;
import com.example.ai.formatter.PromptFormatter;
import com.example.ai.infrastructure.embedding.EmbeddingService;
import com.example.ai.infrastructure.llm.AiModelClient;
import com.example.ai.infrastructure.vectorstores.VectorStore;

@Component
public class RagPipeline {
	
	private final AiModelClientFactory aiModelClientFactory; // AI 模型的客戶端工廠
	private final VectorStoreFactory vectorStoreFactory; // 向量庫工廠
	private final EmbeddingModeFactory embeddingModelFactory; // Embedding模型工廠
	private final PromptFormatter promptFormatter; // 組成prompt
	
	@Autowired
	public RagPipeline(AiModelClientFactory aiModelClientFactory, VectorStoreFactory vectorStoreFactory, EmbeddingModeFactory embeddingModelFactory,PromptFormatter promptFormatter) {
		this.aiModelClientFactory = aiModelClientFactory;
		this.vectorStoreFactory = vectorStoreFactory;
		this.embeddingModelFactory = embeddingModelFactory;
		this.promptFormatter = promptFormatter;
	}
	
	/*
	 * 問答方法：
	 * 將問題向量化 -> 找出最相似的文本段落 -> 組成 Prompt -> 呼叫 LLM 生成答案
	 * */
	public AiResponse query(String question, AiModelType aiModel, EmbeddingModelType embeddingModel, VectorStoreType storeType) {
		EmbeddingService embeddingService = embeddingModelFactory.getEmbeddingService(embeddingModel);
		
		// 將問題轉向量
		float[] queryVector = embeddingService.getEmbedding(question);
		
		// 取得 TopK 最相似的文本段落
		VectorStore vectorStore = vectorStoreFactory.getStore(storeType);
		List<String> topKChunks = vectorStore.similaritySearch(queryVector, 3);
		
		// 組成 prompt
		String prompt = promptFormatter.formatRagPrompt(question, topKChunks);
		
		// 取得對應 LLM ， 生成答案
		AiModelClient client = aiModelClientFactory.getClient(aiModel);
		String answer = client.completion(prompt);

		return new AiResponse(answer);
	}
}
