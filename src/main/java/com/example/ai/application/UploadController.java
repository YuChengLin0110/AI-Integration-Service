package com.example.ai.application;

import java.net.Authenticator.RequestorType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ai.domain.model.DocumentUploadRequest;
import com.example.ai.domain.model.EmbeddingModelType;
import com.example.ai.domain.model.VectorStoreType;
import com.example.ai.domain.service.DocumentService;

@RestController
@RequestMapping("/api/ai/rag")
public class UploadController {
	
	private final DocumentService documentService;
	
	public UploadController(DocumentService documentService) {
		this.documentService = documentService;
	}
	
	/*
	 * 檔案上傳
	 * 可直接使用 MultipartFile：
	 *   @RequestParam MultipartFile file
	 *   @RequestPart MultipartFile file
	 * 
	 * 如果要結合 DTO（文字欄位 + 檔案）：
	 *   @ModelAttribute Dto dto
	 * 
	 * 不可使用 @RequestBody 接收 MultipartFile
	 */
	@PostMapping("/upload")
	public ResponseEntity<String> uploadDocument(@ModelAttribute DocumentUploadRequest request) {
		MultipartFile file = request.getFile();
		EmbeddingModelType model = request.getEmbeddingModel();
		VectorStoreType storeType = VectorStoreType.INMEMORY;
		
		if(file == null || file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
		}
		
		try {
			String documentId = documentService.uploadDocument(file, model, storeType);
			return ResponseEntity.ok(documentId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
		}
	}
	
}
