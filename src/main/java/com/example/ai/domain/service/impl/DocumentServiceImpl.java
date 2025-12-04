package com.example.ai.domain.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ai.domain.model.EmbeddingModelType;
import com.example.ai.domain.service.DocumentService;
import com.example.ai.factory.EmbeddingModeFactory;
import com.example.ai.infrastructure.embedding.EmbeddingService;
import com.example.ai.infrastructure.vectorstores.VectorStore;
import com.example.ai.utils.PdfParser;
import com.example.ai.utils.TextChunker;

@Service
public class DocumentServiceImpl implements DocumentService{
	
	private final EmbeddingModeFactory embeddingModeFactory; // 負責將文字轉為向量
	private final VectorStore vectorStore; // 負責儲存文件段落與對應向量，用於相似度檢索
	
	@Autowired
	public DocumentServiceImpl (EmbeddingModeFactory embeddingModeFactory, VectorStore vectorStore) {
		this.embeddingModeFactory = embeddingModeFactory;
		this.vectorStore = vectorStore;
	}
	
	/*
	 * 上傳 PDF 文件並將內容切段後生成向量，存入 VectorStore
	 * */
	@Override
	public String uploadDocument(MultipartFile file, EmbeddingModelType model) throws Exception {
		// 生成 documentId: UUID + 原始檔名，確保唯一性
		String documentId = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
		
		
		String fileName = file.getOriginalFilename();
		if(fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
			throw new IllegalArgumentException("Only PDF supported");
		}
		
		// 將 MultipartFile 暫存
		File temp = File.createTempFile("upload-", ".pdf");
		file.transferTo(temp);
		
		// 解析 PDF 成文字
		String text = PdfParser.parseToText(temp);
		
		// 檢查檔案是否為空
		if(text.trim().isEmpty()) {
			throw new IllegalArgumentException("Parsed PDF is empty");
		}
		
		// 切段：TextChunker.chunkSmart(text, 500, 200, 100)
        // 500: chunk 最大長度
        // 200: overlap 長度
        // 100: 最小 chunk 長度
		List<String> chunks = TextChunker.chunkSmart(text, 500, 200, 100);
		
		// 生成每個 chunk 的向量
		EmbeddingService embeddingService = embeddingModeFactory.getEmbeddingService(model);
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
