package com.example.ai.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
	String uploadDocument(MultipartFile file) throws Exception;
}
