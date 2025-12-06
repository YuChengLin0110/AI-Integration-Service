package com.example.ai.formatter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.ai.core.prompt.PromptBuilder;

@Component
public class DefaultPromptFormatter implements PromptFormatter{
	
	/*
	 * RAG 用的生成 Prompt
	 * */
	@Override
	public String formatRagPrompt(String question, List<String> chunks) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("根據以下內容回答問題：\n\n");
//		chunks.forEach(chunk -> sb.append(chunk).append("\n"));
//		sb.append("\n 問題：").append(question);
		
		// 將所有 chunk 以換行合併成一個字串
		String allChunks = String.join("\n ", chunks);
		
		// 使用 PromptBuilder 生成最終 prompt
		// 模板中用 {{allChunks}} 與 {{question}} 占位，最後用 addVariables 替換
		return new PromptBuilder()
				.setTemplate("""
						根據以下內容回答問題：
						{{allChunks}}
						
						 問題：
						 {{question}}
						""")
				.addvariables("allChunks", allChunks)
				.addvariables("question", question)
				.build();
	}
	
	/*
	 * 生成一個簡單問答 prompt
	 * */
	@Override
	public String formatSimplePrompt(String question) {
		return new PromptBuilder()
				.setTemplate("回答以下問題：{{question}}")
				.addvariables("question", question)
				.build();
	}

}
