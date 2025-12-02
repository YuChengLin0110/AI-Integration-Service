package com.example.ai.core.prompt;

import java.util.Map;

/**
 * PromptTemplate 用來處理模板字串
 * 將模板中的 {{key}} 替換為對應的值
 * 範例：
 *   模板: "問題: {{question}}，上下文: {{context}}"
 *   values: {"question": "今天天氣如何？", "context": "天氣預報顯示..."}
 *   render() 後輸出: "問題: 今天天氣如何？，上下文: 天氣預報顯示..."
 */
public class PromptTemplate {
	
	private String template;
	
	public PromptTemplate(String template) {
		this.template = template;
	}
	
	/*
	 * 將模板中的 {{key}} 替換成 values 裡對應的 value
	 * */
	public String render(Map<String, String> values) {
		String result = template;
		
		// 遍歷所有 key-value，將模板中的 {{key}} 替換成 value
		for(Map.Entry<String, String> entry : values.entrySet()) {
			result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
		}
		
		return result;
	}
}
