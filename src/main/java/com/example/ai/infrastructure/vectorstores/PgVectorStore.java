package com.example.ai.infrastructure.vectorstores;

import java.util.ArrayList;
import java.util.List;

import com.example.ai.domain.model.VectorDocument;
import com.example.ai.domain.model.VectorStoreType;
import com.example.ai.infrastructure.llm.mybatis.mapper.VectorDocumentMapper;

/**
 * PgVectorStore：基於 PostgreSQL + pgvector 的向量儲存實作
 * 用於儲存向量並提供相似度搜索功能
 */
public class PgVectorStore implements VectorStore{
	
	private final VectorDocumentMapper mapper;
	private static final int BATCH_SIZE = 200;
	
	public PgVectorStore(VectorDocumentMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public VectorStoreType getVectorStoreType() {
		return VectorStoreType.PGVECTOR;
	}
	
	/**
     * 新增文件及其對應向量
     *
     * docId 對應上傳文件，用於追蹤來源文件
     *
     * 驗證 chunks 與 embeddings 數量是否一致
     * 每 BATCH_SIZE 筆進行一次批量插入
     * 插入完畢後，處理剩餘不足一批的資料
     *
     * 目前是 MyBatis foreach
     * 可新增 batch
     * 如果 chunks 很大，可分批插入以避免單條 SQL 過長
     */
	@Override
	public void addDocument(String docId, List<String> chunks, List<float[]> embeddings) {
		if(chunks.size() != embeddings.size()) {
			throw new IllegalArgumentException("Chunks and embeddings size mismatch");
		}
		
		List<VectorDocument> batch = new ArrayList<>();
		
		for(int i = 0 ; i < chunks.size() ; i++) {
			VectorDocument doc = new VectorDocument();
			doc.setId(docId);
			doc.setChunk(chunks.get(i));
			doc.setEmbedding(embeddings.get(i));
			batch.add(doc);
			
			 // 達到 BATCH_SIZE 就批量插入
			if(batch.size() >= BATCH_SIZE) {
				mapper.insertDocumentBatch(batch);
				batch.clear();
			}
		}
		
		// 插入剩餘不足 BATCH_SIZE 的資料
		if(!batch.isEmpty()) {
			mapper.insertDocumentBatch(batch);
		}
		
	}
	
	/**
     * 相似度搜索 topK chunk
     * 返回 topK 結果
     */
	@Override
	public List<String> similaritySearch(float[] queryVec, int topK) {
		
		return mapper.selectTopKSimilar(queryVec, topK);
	}

}
