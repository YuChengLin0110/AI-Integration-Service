package com.example.ai.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.example.ai.infrastructure.embedding.EmbeddingService;

/*
 * 將多段文字平行生成向量
 * */
public class EmbeddingBatcher {
	
	// 執行緒數量
	private static final int THREADS = Runtime.getRuntime().availableProcessors();
	
	/*
	 * 將 chunks，使用指定 EmbeddingService 生成向量
	 * */
	public static List<float[]> embedAll(List<String> chunks, EmbeddingService service) {
		
		// 建立固定大小的 ThreadPool
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);
		List<Future<float[]>> futures = new ArrayList<>();
		
		 // 提交每個 chunk 的向量生成任務
		for(String chunk : chunks) {
			futures.add(executor.submit(() -> service.getEmbedding(chunk)));
		}
		
		// 收集結果
		List<float[]> result = new ArrayList<>();
		for(Future<float[]> future : futures) {
			try {
				result.add(future.get());
			} catch (Exception e) {
				// log
				result.add(new float[0]);
			}
		}
		
		// 關閉 ThreadPool
		executor.shutdown();
		
		return result;
	}
	
}
