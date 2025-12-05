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
import com.example.ai.utils.EmbeddingBatcher;
import com.example.ai.utils.PdfParser;
import com.example.ai.utils.TextChunker;

@Service
public class DocumentServiceImpl implements DocumentService {

	private final EmbeddingModeFactory embeddingModeFactory; // 負責將文字轉為向量
	private final VectorStore vectorStore; // 負責儲存文件段落與對應向量，用於相似度檢索
	private final int BATCH_SIZE = 20;

	@Autowired
	public DocumentServiceImpl(EmbeddingModeFactory embeddingModeFactory, VectorStore vectorStore) {
		this.embeddingModeFactory = embeddingModeFactory;
		this.vectorStore = vectorStore;
	}

	/*
	 * 上傳 PDF 文件並將內容切段後生成向量，存入 VectorStore
	 * 將 PDF 以每 BATCH_SIZE 頁為一批次讀取 
	 * 將每批文字切割成多個 chunk
	 * 透過多執行緒平行發送 embedding 請求 以提升效能 
	 */
	@Override
	public String uploadDocument(MultipartFile file, EmbeddingModelType model) throws Exception {
		// 取得原始檔名並驗證格式
		String fileName = file.getOriginalFilename();
		if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
			throw new IllegalArgumentException("Only PDF supported");
		}

		// 生成唯一 documentId: UUID + 原始檔名
		String documentId = UUID.randomUUID().toString() + "-" + fileName;

		// 將 MultipartFile 暫存為本地檔案
		File temp = File.createTempFile("upload-", ".pdf");
		file.transferTo(temp);

		try {
			// 取得 PDF 總頁數
			int totalPages = PdfParser.getPageCount(temp);
			
			// 取得對應的 EmbeddingService
			EmbeddingService embeddingService = embeddingModeFactory.getEmbeddingService(model);
			
			// 分批解析 PDF，每批 BATCH_SIZE 頁
			for (int startPage = 1; startPage <= totalPages; startPage += BATCH_SIZE) {
				int endPage = Math.min(startPage + BATCH_SIZE, totalPages);
				String batchText = PdfParser.parseToText(temp, startPage, endPage);

				if (batchText.trim().isEmpty()) {
					continue; // 若該批頁沒有文字，跳過
				}

				// 切段：TextChunker.chunkSmart(text, 500, 200, 100)
				// 500: chunk 最大長度
				// 200: overlap 長度
				// 100: 最小 chunk 長度
				List<String> chunks = TextChunker.chunkSmart(batchText, 500, 200, 100);

				// 多執行續處理 Embedding
				List<float[]> embeddings = EmbeddingBatcher.embedAll(chunks, embeddingService);

				// 將 chunk 與 embeddings 存入 VectorStore
				vectorStore.addDocument(documentId, chunks, embeddings);
			}
		} finally {
			temp.delete(); // 確保暫存檔案被刪除
		}
		
		return documentId;
	}

}
