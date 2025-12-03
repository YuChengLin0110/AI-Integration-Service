# AI-Integration-Service

RAG（Retrieval-Augmented Generation）專案，可將私有文件轉向量、存入向量庫，並結合不同 LLM (OpenAI、Gemini 等）做問答  
流程：上傳 -> 分段 -> Embedding -> VectorStore -> 問答

## 功能說明

### 上傳文件
- 自動切段 chunk
- 每段生成向量 embedding
- 存入 Vector

### RAG 問答
- 用向量資料庫搜尋 Top-K 相關段落
- 將最相似段落組成 prompt 給 AI
- 回傳回答

### AI Chat（非 RAG）
- 單純呼叫選定的 LLM 模型
- 無參考私有資料，用於一般聊天

## 專案技術
- Spring Boot REST API
- 架構可擴展至多模型（ OpenAI、Gemini 或其他 LLM ）
- RAG Pipeline 將私有文件與 LLM 結合，保證回答準確度

## 模型整合架構（Factory + Strategy Pattern）

本專案支援多個 LLM（如 OpenAI、Gemini）

為避免程式碼寫死依賴某個模型，採用 工廠模式 + 策略模式 管理不同模型的呼叫方式 : 

- AiModelType（enum）：定義可選模型
- 各模型實作（OpenAiClient、GeminiClient 等）：封裝各自 SDK 與差異
- AiModelFactory：根據 AiModelType 回傳對應的 AiModelClient 實例
- 新增模型只需新增 enum & 實作，不需修改現有程式碼
