package com.example.ai.core.prompt;

import java.util.HashMap;
import java.util.Map;

/*
 * PromptBuilder 用來方便構建 Prompt 字串
 * */
public class PromptBuilder {
	private String template; // Prompt 模板字串，例如 "問題: {{question}}，上下文: {{context}}"
	private Map<String, String> variables = new HashMap<>(); // 存放要替換的 key-value
	
	public PromptBuilder setTemplate(String template) {
		this.template = template;
		return this;
	}
	
	/*
	 * 新增或更新模板中要替換的值
	 * */
	public PromptBuilder addvariables(String key, String value) {
		variables.put(key, value);
		return this;
	}
	
	/**
     * 生成最終的 Prompt 字串
     * 將模板中的 {{key}} 替換成對應的 value
     */
	public String build() {
		PromptTemplate pt = new PromptTemplate(template);
		return pt.render(variables);
	}
}
