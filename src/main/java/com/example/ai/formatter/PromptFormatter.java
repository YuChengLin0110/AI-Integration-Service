package com.example.ai.formatter;

import java.util.List;

public interface PromptFormatter {
	String formatRagPrompt(String question, List<String> chunks);
	String formatSimplePrompt(String question);
}
