package com.example.ai.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ai.domain.model.EmbeddingModelType;
import com.example.ai.domain.service.DocumentService;

@RestController
@RequestMapping("/api/ai/rag")
public class UploadController {
	
	private final DocumentService documentService;
	
	public UploadController(DocumentService documentService) {
		this.documentService = documentService;
	}
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file, @RequestParam(defaultValue = "DUMMY") EmbeddingModelType model) {
		if(file == null || file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
		}
		
		try {
			String documentId = documentService.uploadDocument(file, model);
			return ResponseEntity.ok(documentId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
		}
	}
	
}
