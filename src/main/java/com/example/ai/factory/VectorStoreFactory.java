package com.example.ai.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ai.domain.model.VectorStoreType;
import com.example.ai.infrastructure.vectorstores.VectorStore;

@Component
public class VectorStoreFactory {
	
	private final Map<VectorStoreType, VectorStore> storeMap;
	
	@Autowired
	public VectorStoreFactory(List<VectorStore> stores) {
		storeMap = new HashMap<>();
		for(VectorStore store : stores) {
			storeMap.put(store.getVectorStoreType(), store);
		}
	}
	
	public VectorStore getStore(VectorStoreType type) {
		return storeMap.get(type);
	}
}
