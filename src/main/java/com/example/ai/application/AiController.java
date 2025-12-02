package com.example.ai.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ai.domain.model.AiModelType;
import com.example.ai.domain.model.AiResponse;
import com.example.ai.domain.model.PromptRequest;
import com.example.ai.domain.service.AiService;
import com.example.ai.factory.AiServiceFactory;

@RestController
@RequestMapping("/api/ai")
public class AiController {
	
	private final AiServiceFactory aiServiceFactory;
	
	@Autowired
	public AiController(AiServiceFactory aiServiceFactory) {
		this.aiServiceFactory = aiServiceFactory;
	}
	
	@PostMapping("/completion")
	public AiResponse completion(@RequestBody PromptRequest request, @RequestParam(defaultValue = "DUMMY") AiModelType model) {
		AiService service = aiServiceFactory.getService(model);
		if(service == null) {
			throw new IllegalArgumentException("No AiService for model: " + model);
		}
		
		return service.complete(request);
	}
	
}
