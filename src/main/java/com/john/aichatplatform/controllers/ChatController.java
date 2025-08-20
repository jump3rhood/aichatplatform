package com.john.aichatplatform.controllers;

import com.john.aichatplatform.configs.ConversationIdGenerator;
import com.john.aichatplatform.services.ChatService;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/chat")
@RestController
public class    ChatController {

    private final ChatService chatService;
    private final ConversationIdGenerator idGenerator;
    public ChatController(ChatService chatService, ConversationIdGenerator idGenerator){
        this.chatService = chatService;
        this.idGenerator = idGenerator;
    }


    @PostMapping("/simple")
    public ResponseEntity<String> simpleChat(@RequestBody Map<String, String> request){
        String response = chatService.simpleChat(request.get("message"));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/memory")
    public ResponseEntity<Map<String, Object>> memoryChat(@RequestBody Map<String, String> request){
        String message = request.get("message");
        String conversationId = request.get("conversationId");
        if(conversationId == null || conversationId.trim().isEmpty()){
            conversationId = idGenerator.generateId();
        }
        String response = chatService.memoryChat(message, conversationId);

        return ResponseEntity.ok(Map.of(
                "response", response,
                "conversationId", conversationId,
                "isNewConversation", request.get("conversationId") == null
        ));
    }

    @GetMapping("/conversations/{conversationId}/history")
    public ResponseEntity<List<Message>> getAllConversations(@PathVariable String conversationId){
        List<Message> history = chatService.getConversationHistory(conversationId);
        return ResponseEntity.ok(history);
    }
    @PostMapping("/conversations/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@PathVariable String conversationId){
        chatService.deleteConversation(conversationId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
