package com.john.aichatplatform.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ChatService {
    private final Map<String, String> systemPrompts;
    private final ChatClient simpleChatClient;
    private final ChatClient memoryChatClient;
    private final ChatMemory chatMemory;
    public ChatService(OpenAiChatModel openAiChatModel, ChatMemory chatMemory, Map<String, String> systemPrompts) {
        System.out.println("OpenAI Model: " + openAiChatModel.getClass().getName());
        System.out.println("Model Options: " + openAiChatModel.getDefaultOptions());
        this.systemPrompts = systemPrompts;
        this.chatMemory = chatMemory;
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        this.simpleChatClient = ChatClient.builder(openAiChatModel).build();
        this.memoryChatClient = ChatClient
                .builder(openAiChatModel)
                .defaultAdvisors(messageChatMemoryAdvisor)
                .build();
    }

    public String simpleChat(String message){
        return simpleChatClient.prompt()
                .system(systemPrompts.get("simple"))
                .user(message)
                .call()
                .content();
    }

    public String memoryChat(String message, String conversationId){

        return memoryChatClient.prompt()
                .system(systemPrompts.get("chat"))
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    // Get Conversation History
    public List<Message> getConversationHistory(String conversationId){
        return chatMemory.get(conversationId);
    }

    // Clear conversation
    public void deleteConversation(String conversationId){
        chatMemory.clear(conversationId);
    }
g
}
