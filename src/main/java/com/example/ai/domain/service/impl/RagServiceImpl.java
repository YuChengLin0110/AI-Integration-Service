package com.example.ai.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ai.core.pipeline.RagPipeline;
import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;
import com.example.ai.domain.service.RagService;

@Service
public class RagServiceImpl implements RagService{
	
	private final RagPipeline pipeline;
	
	@Autowired
	public RagServiceImpl(RagPipeline pipeline) {
		this.pipeline = pipeline;
	}
	
	@Override
	public AiResponse query(String question, AiModelType model) {
		return pipeline.query(question, model);
	}

//	@Override
//	public void uploadPdf(MultipartFile file) throws Exception {
//		// 轉 文字
//		InputStream input = file.getInputStream();
//		Scanner scanner = new Scanner(input);
//		StringBuilder text = new StringBuilder();
//		
//		while(scanner.hasNextLine()) {
//			text.append(scanner.nextLine()).append("\n");
//		}
//		
//		// chunk
//		List<String> chunks = new ArrayList<>();
//		String content = text.toString();
//		int chunkSize = 500;
//		
//		for(int i = 0 ; i < content.length() ; i+= chunkSize) {
//			int end = Math.min(i + chunkSize, content.length());
//			chunks.add(content.substring(i, end));
//		}
//		
//		// Embedding
//		List<float[]> embeddings = new ArrayList<>();
//		for(String chunk : chunks) {
//			embeddings.add(embeddingService.getEmbedding(chunk));
//		}
//		
//		// 存入 VectorStore
//		vectorStore.addDocument(file.getOriginalFilename(), chunks, embeddings);
//	}

}
