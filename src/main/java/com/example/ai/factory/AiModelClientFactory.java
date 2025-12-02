package com.example.ai.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.infrastructure.llm.AiModelClient;

@Component
public class AiModelClientFactory {
	
	private final Map<AiModelType, AiModelClient> clientMap;
	
	@Autowired
	public AiModelClientFactory(List<AiModelClient> clients) {
		clientMap = new HashMap<>();
		for(AiModelClient client : clients) {
			clientMap.put(client.getModelType(), client);
		}
	}
	
	public AiModelClient getClient(AiModelType model) {
		return clientMap.get(model);
	}
	
}
