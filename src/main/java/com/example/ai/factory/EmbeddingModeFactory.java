package com.example.ai.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ai.domain.model.EmbeddingModelType;
import com.example.ai.infrastructure.embedding.EmbeddingService;

@Component
public class EmbeddingModeFactory {
	
	private final Map<EmbeddingModelType, EmbeddingService> embeddingMap;
	
	@Autowired
	public EmbeddingModeFactory (List<EmbeddingService> services) {
		embeddingMap = new HashMap<>();
		for(EmbeddingService service : services) {
			embeddingMap.put(service.getModelType(), service);
		}
	}
	
	public EmbeddingService getEmbeddingService(EmbeddingModelType model) {
		return embeddingMap.get(model);
	}

}
