package com.john.aichatplatform.configs;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ChatConfig {

    @Bean
    public Map<String, String> systemPrompts(){
        Map<String, String> prompts = new HashMap<>();
        prompts.put("simple", """
            You are a helpful AI assistant. Provide direct, concise answers.
            """);

        prompts.put("chat", """
            You are a friendly AI assistant having a conversation.
            Use conversation history to provide contextual responses.
            """);

        prompts.put("pdf", """
            You are an AI assistant helping users understand documents.
            Use the provided document context to answer questions accurately.
            """);
        prompts.put("rag", """
                Based on the following context from the document, please answer the user's question.
                
                            Context:
                            %s
                
                            Question: %s
                
                            If the answer cannot be found in the context, please say so.
                """);
        return prompts;
    }

    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
    }

}
