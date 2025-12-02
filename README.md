# AI-Integration-Service

RAG（Retrieval-Augmented Generation）專案，可將私有文件轉向量、存入向量庫，並結合不同 LLM (OpenAI、Gemini 等）做問答  
流程：上傳 PDF/文字 -> 分段 -> Embedding -> VectorStore -> 問答

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
