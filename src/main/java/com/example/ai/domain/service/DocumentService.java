package com.example.ai.domain.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.ai.domain.model.EmbeddingModelType;

public interface DocumentService {
	String uploadDocument(MultipartFile file, EmbeddingModelType model) throws Exception;
}
