package com.example.ai.domain.model;

public class VectorDocument {
	private String id;
	private String docId;
    private String chunk;
    private float[] embedding;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getChunk() {
		return chunk;
	}
	public void setChunk(String chunk) {
		this.chunk = chunk;
	}
	public float[] getEmbedding() {
		return embedding;
	}
	public void setEmbedding(float[] embedding) {
		this.embedding = embedding;
	}
}
