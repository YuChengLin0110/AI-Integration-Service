package com.example.ai.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ai.domain.model.AiResponse;
import com.example.ai.domain.model.RagQueryRequest;
import com.example.ai.domain.model.VectorStoreType;
import com.example.ai.domain.service.RagService;

@RestController
@RequestMapping("/api/ai/rag")
public class RagController {
	
	private final RagService ragService;
	
	@Autowired
	public RagController(RagService ragService) {
		this.ragService = ragService;
	}
	
	// 問答
	@PostMapping("/query")
	public AiResponse query(@RequestBody RagQueryRequest request) {
		VectorStoreType storeType = VectorStoreType.INMEMORY;
		return ragService.query(request.getQuestion(), request.getAiModel(), request.getEmbeddingModel(), storeType);
	}
	
	// PDF 上傳
//	@PostMapping("/upload")
//	public ResponseEntity<String> uploadPdf(@RequestParam MultipartFile file) {
//		try {
//			ragService.uploadPdf(file);
//			return ResponseEntity.ok("PDF 上傳成功");
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("PDF 上傳失敗");
//		}
//	}
}
