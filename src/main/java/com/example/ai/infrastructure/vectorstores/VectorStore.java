package com.example.ai.infrastructure.vectorstores;

import java.util.List;

import com.example.ai.domain.model.VectorStoreType;

public interface VectorStore {
	VectorStoreType getVectorStoreType();
	void addDocument(String docId, List<String> chunks, List<float[]> embeddings);
	List<String> similaritySearch(float[] queryVec, int topK);
}
