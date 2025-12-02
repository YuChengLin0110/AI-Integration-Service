package com.example.ai.utils;

import java.util.ArrayList;
import java.util.List;

public class TextChunker {
	
	/*
	 * 將文字切成多個 chunk（段落），每個 chunk 大小介於 minChunkSize 與 maxChunkSize
	 * 支援句子級切割，並可設定重疊字數（overlap）
	 * */
	public static List<String> chunkSmart(String text, int maxChunkSize, int minChunkSize, int overlap) {
		List<String> chunks = new ArrayList<>();
		if (text == null || text.trim().isEmpty()) {
			return chunks;
		}

		// 統一換行符號 & 去除多餘空格
		String cleaned = text.replace("\r\n", "\n").replace("\r", "\n").replaceAll("[ ]{2,}", " ").trim();

		// 用句子切割文字
		List<String> sentences = splitToSentences(cleaned);

		// 將句子組合成 chunk
		StringBuilder sb = new StringBuilder();

		for (String sentence : sentences) {

			// 若加入句子後超過最大長度
			if (sb.length() + sentence.length() > maxChunkSize) {
				
				// 若目前 chunk 長度足夠，先存下
				if (sb.length() >= minChunkSize) {
					chunks.add(sb.toString().trim());
					
					// 支援 overlap（重疊部分保留到下一個 chunk）
					if (overlap > 0) {
						String lastPart = getLastPart(sb, overlap);
						sb = new StringBuilder(lastPart);
					} else {
						sb = new StringBuilder();
					}
				}
			}

			sb.append(sentence).append(" ");
		}
		
		// 避免最後 chunk 遺漏
		if(sb.length() > 0) {
			chunks.add(sb.toString().trim());
		}
		
		return chunks;
	}

	/*
	 * 使用正則表達式將文字切成句子
	 * 會以「。！？!? .」為句尾
	 */
	private static List<String> splitToSentences(String text) {
		List<String> sentences = new ArrayList<>();
		
		// 切割句子
		String[] parts = text.split("(?<=[。！？!?\\.])");

		for (String part : parts) {
			String s = part.trim();
			if (!s.isEmpty()) {
				sentences.add(s);
			}
		}

		return sentences;
	}
	
	/*
	 * 取得 StringBuilder 的最後 overlap 長度字串
	 * 用於 chunk 重疊
	 * */
	private static String getLastPart(StringBuilder sb, int overlap) {
		int length = sb.length();
		if (length <= overlap) {
			return sb.toString();
		}

		return sb.substring(length - overlap);
	}

}
