package com.example.ai.infrastructure.llm;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.example.ai.domain.model.AiModelType;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

@Component("OPENAI")
@ConditionalOnProperty(name = "openai.enabled", havingValue = "true", matchIfMissing = false)
public class OpenAiClientImpl implements AiModelClient {
	
	private OpenAIClient client;
	
	@Autowired
	public OpenAiClientImpl(OpenAIClient client) {
		this.client = client;
	}
	
	@Override
	public AiModelType getModelType() {
		return AiModelType.OPENAI;
	}
	
	/*
	 * 生成 AI 回應
	 * */
	@Override
	public String completion(String prompt) {
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
				.model(ChatModel.GPT_3_5_TURBO)
				.addUserMessage(prompt) // 將 prompt 當作使用者訊息
				.build();
		
		//  呼叫 OpenAI API
		ChatCompletion chat = client.chat().completions().create(params);
		
		// 取得回應內容
		Optional<String> messageOpt = chat.choices().get(0).message().content();
		
		if(messageOpt.isPresent()) {
			return messageOpt.get();
		}else {
			return "";
		}
	}

}
