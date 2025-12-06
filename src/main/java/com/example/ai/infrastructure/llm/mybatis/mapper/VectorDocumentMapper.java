package com.example.ai.infrastructure.llm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.ai.domain.model.VectorDocument;

@Mapper
public interface VectorDocumentMapper {
	
	/**
     * 單筆 insert
     */
	@Insert("""
			INSERT INTO documents(id, chunk, embedding) 
			VALUES (#{id}, #{docId}, #{chunk}, #{embedding})
			""")
	void insertDocument(VectorDocument doc);
	
	// 批量 insert
	@Insert("""
			<script>
			INSERT INTO documents(id, chunk, embedding)
			VALUES
			<foreach collection='docs' item='doc' separator=','>
				(#{doc.id}, #{doc.docId}, #{doc.chunk}, #{doc.embedding})
			</foreach>
			</script>
			""")
	void insertDocumentBatch(@Param("docs") List<VectorDocument> docs);
	
	/**
     * 查詢與 query 向量最相似的 topK chunk
     */
	@Select("""
			SELECT chunk
			FROM documents
			ORDER BY embedding <-> #{query} LIMIT #{topK}
			""")
	List<String> selectTopKSimilar(@Param("query") float[] query, @Param("topK") int topK);
}
