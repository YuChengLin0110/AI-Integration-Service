package com.example.ai.utils;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/*
 * 將 PDF 檔案解析為純文字
 * */
public class PdfParser {
	
	public static String parseToText(File file) throws IOException {
		try (PDDocument  doc = Loader.loadPDF(file)) {
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true); // 設定按位置排序文字，保持原本排版順序
			return stripper.getText(doc); // 取得 PDF 文字
		}
	}
	
}
