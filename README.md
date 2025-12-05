# AI-Integration-Service

AI RAG（Retrieval-Augmented Generation）專案，將私有文件轉為向量存入向量庫，並結合不同 LLM（OpenAI、Gemini 等）進行問答

核心流程：文件上傳 → 分段 → Embedding → 向量存儲 → 問答

## 功能說明

### 上傳文件
- 自動將文件切分為多個段落 chunk
- 每段生成向量 embedding
- 存入向量資料庫（VectorStore），方便後續相似度檢索

### RAG 問答
- 對使用者問題進行向量化
- 從向量庫檢索 Top-K 最相似段落
- 組成 prompt 提供給 LLM，產生精準回答
- 適用於私有文件輔助問答場景

### AI Chat（非 RAG）
- 單純呼叫選定的 LLM 模型
- 不依賴私有資料，用於一般對話或聊天場景

## 專案技術
- Spring Boot REST API
- 模組化架構，支援多模型整合（OpenAI、Gemini 或其他 LLM）
- RAG Pipeline 將私有文件與 LLM 結合，提升問答準確度
- 向量資料庫（VectorStore）實作 Top-K 相似度檢索
- PromptFormatter / PromptBuilder 統一管理 prompt 組裝

## 模型整合架構（Factory + Strategy Pattern）

本專案支援多個 LLM（如 OpenAI、Gemini）

為避免程式碼寫死依賴某個模型，採用 工廠模式 + 策略模式 管理不同模型的呼叫方式 : 

- AiModelType（enum）：定義可選模型
- 各模型實作（OpenAiClient、GeminiClient 等）：封裝各自 SDK 與差異
- AiModelFactory：根據 AiModelType 回傳對應的 AiModelClient 實例
- 擴展性高：新增模型僅需新增 enum 與實作類別，無需修改現有程式碼
