package com.example.ai.infrastructure.vectorstores;

import java.util.List;

public interface VectorStore {
	void addDocument(String docId, List<String> chunks, List<float[]> embeddings);
	List<String> similaritySearch(String query, int topK);
}
