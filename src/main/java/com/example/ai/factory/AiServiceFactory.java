package com.example.ai.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.service.AiService;

@Component
public class AiServiceFactory {
	
	private final Map<AiModelType, AiService> serviceMap;
	
	@Autowired
	public AiServiceFactory(List<AiService> services) {
		serviceMap = new HashMap<>();
		for(AiService service : services) {
			serviceMap.put(service.getModelType(), service);
		}
	}
	
	public AiService getService(AiModelType model) {
		AiService service = serviceMap.get(model);
		if(service == null) {
			throw new IllegalArgumentException("No AiService for model: " + model);
		}
		
		return service;
	}
	
}
