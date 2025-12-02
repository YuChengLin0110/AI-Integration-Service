package com.example.ai.domain.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ai.domain.service.DocumentService;
import com.example.ai.infrastructure.embedding.EmbeddingService;
import com.example.ai.infrastructure.vectorstores.VectorStore;
import com.example.ai.utils.TextChunker;

@Service
public class DocumentServiceImpl implements DocumentService{
	
	private final EmbeddingService embeddingService; // 負責將文字轉為向量
	private final VectorStore vectorStore; // 負責儲存文件段落與對應向量，用於相似度檢索
	
	@Autowired
	public DocumentServiceImpl (EmbeddingService embeddingService, VectorStore vectorStore) {
		this.embeddingService = embeddingService;
		this.vectorStore = vectorStore;
	}
	
	/*
	 * 上傳文件並將內容切段後生成向量，存入 VectorStore
	 * */
	@Override
	public String uploadDocument(MultipartFile file) throws Exception {
		// 生成 documentId: UUID + 原始檔名，確保唯一性
		String documentId = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
		
		// 讀出文字內容
		InputStream input = file.getInputStream();
		Scanner scanner = new Scanner(input, "UTF-8");
		StringBuilder sb = new StringBuilder();
		while(scanner.hasNextLine()) {
			sb.append(scanner.nextLine()).append("\n");
		}
		scanner.close();
		String text = sb.toString();
		
		// 檢查檔案是否為空
		if(text.trim().isEmpty()) {
			throw new IllegalArgumentException("file content is empty");
		}
		
		// 切段：TextChunker.chunkSmart(text, 500, 200, 100)
        // 500: chunk 最大長度
        // 200: overlap 長度
        // 100: 最小 chunk 長度
		List<String> chunks = TextChunker.chunkSmart(text, 500, 200, 100);
		
		// 為每個 chunk 產生 embedding
		List<float[]> embeddings = new ArrayList<>();
		for(int i = 0 ; i < chunks.size() ; i++) {
			String chunk = chunks.get(i);
			float[] vec = embeddingService.getEmbedding(chunk);
			embeddings.add(vec);
			
			// log
		}
		
		// 將 chunk 與 embeddings 存入 VectorStore
		vectorStore.addDocument(documentId, chunks, embeddings);
		// log
		
		return documentId;
	}

}
