package com.example.ai.infrastructure.vectorstores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.stereotype.Component;

@Component
public class InMemoryVectorStore implements VectorStore{
	
	private Map<String, List<float[]>> vectorMap = new HashMap<>(); // 文件 ID -> 向量列表
	private Map<String, List<String>> chunkMap = new HashMap<>(); // 文件 ID -> 對應文字段落
	
	/*
     * 新增文件的向量與段落
     */
	@Override
	public void addDocument(String docId, List<String> chunks, List<float[]> embeddings) {
		vectorMap.put(docId, embeddings);
		chunkMap.put(docId, chunks);
	}
	
	/*
     * 相似度檢索 topK 段落
     */
	@Override
	public List<String> similaritySearch(String queryEmbeddingStr, int topK) {
		// 將字串轉成 float[]
		String[] parts = queryEmbeddingStr.split(",");
		float[] queryVec = new float[parts.length];
		for(int i = 0 ; i < parts.length ; i++) {
			queryVec[i] = Float.parseFloat(parts[i]);
		}
		
		// 以 min-heap 取 topK 分數高的
		PriorityQueue<ScoredChunk> scoredHeap = new PriorityQueue<>((a, b) -> Float.compare(a.score, b.score));
		// 遍歷所有文件和向量，計算相似度
		for(String docId : vectorMap.keySet()) {
			List<float[]> vecs = vectorMap.get(docId);
			List<String> chunks = chunkMap.get(docId);
			for(int i = 0 ; i < vecs.size() ; i++) {
				float sim = cosineSimilarity(queryVec, vecs.get(i));
				scoredHeap.add(new ScoredChunk(chunks.get(i), sim));
				
				// 若超過 topK，就丟掉最小的
				if(scoredHeap.size() > topK) {
					scoredHeap.poll();
				}
			}
		}
		
		// 取出結果（此時是由小到大）
		List<String> topChunks = new ArrayList<>();
		while(!scoredHeap.isEmpty()) {
			topChunks.add(scoredHeap.poll().chunk);
		}
		
		// 反轉變成大到小
		Collections.reverse(topChunks);
		
		return topChunks;
	}
	
	/*
     * 計算兩個向量的餘弦相似度
     */
	private float cosineSimilarity(float[] a, float[] b) {
		float dot = 0f;
		float normA = 0f;
		float normB = 0f;
		for(int i = 0 ; i < a.length ; i++) {
			dot += a[i] * b[i];
			normA += a[i] * a[i];
			normB += b[i] * b[i];
		}
		
		return (float) (dot / (Math.sqrt(normA) * Math.sqrt(normB)));
	}
	
	/**
     * 封裝段落與相似度分數
     */
	static class ScoredChunk {
		String chunk;
		float score;
		
		ScoredChunk(String chunk, float score) {
			this.chunk = chunk;
			this.score = score;
		}
	}

}
