package com.example.ai.core.pipeline;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;
import com.example.ai.factory.AiModelClientFactory;
import com.example.ai.infrastructure.embedding.EmbeddingService;
import com.example.ai.infrastructure.llm.AiModelClient;
import com.example.ai.infrastructure.vectorstores.VectorStore;

@Component
public class RagPipeline {
	
	private final AiModelClientFactory aiModelClientFactory;
	private final VectorStore vectorStore;
	private final EmbeddingService embeddingService;
	
	@Autowired
	public RagPipeline(AiModelClientFactory aiModelClientFactory, VectorStore vectorStore, EmbeddingService embeddingService) {
		this.aiModelClientFactory = aiModelClientFactory;
		this.vectorStore = vectorStore;
		this.embeddingService = embeddingService;
	}
	
	/*
	 * 問答方法：
	 * 將問題向量化 -> 找出最相似的文本段落 -> 組成 Prompt -> 呼叫 LLM 生成答案
	 * */
	public AiResponse query(String question, AiModelType model) {
		
		// 將問題轉向量
		float[] queryVector = embeddingService.getEmbedding(question);
		
		// 將向量轉成逗號分隔的字串，方便 VectorStore 接收
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < queryVector.length ; i++) {
			sb.append(queryVector[i]);
			if(i != queryVector.length -1) {
				sb.append(",");
			}
		}
		
		String queryStr = sb.toString();
		
		// 取得 TopK 最相似的文本段落
		List<String> chunks = vectorStore.similaritySearch(queryStr, 3);
		
		// 組合 prompt 給 LLM，格式：
		// 「根據以下內容回答問題：<chunk1> <chunk2> <chunk3>...  問題：<question>」
		StringBuilder promptBuilder = new StringBuilder();
		promptBuilder.append("根據以下內容回答問題 : \n");
		chunks.forEach(chunk -> promptBuilder.append(chunk).append("\n"));
		promptBuilder.append("問題 : ").append(question);
		
		// 取得對應 LLM ， 生成答案
		AiModelClient client = aiModelClientFactory.getClient(model);
		String answer = client.completion(promptBuilder.toString());

		return new AiResponse(answer);
	}
}
