package com.john.aichatplatform.controllers;

import com.john.aichatplatform.services.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }

            if (!"application/pdf".equals(file.getContentType())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only PDF files are supported"));
            }

            // Process and store document
            String documentId = documentService.processAndStoreDocument(file);

            return ResponseEntity.ok(Map.of(
                    "documentId", documentId,
                    "fileName", file.getOriginalFilename(),
                    "message", "Document uploaded and processed successfully"
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to process document: " + e.getMessage()
            ));
        }
    }
}
