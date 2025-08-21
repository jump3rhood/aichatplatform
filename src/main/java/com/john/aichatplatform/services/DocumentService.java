package com.john.aichatplatform.services;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentService {
    private final VectorStore vectorStore;
    private final TokenTextSplitter tokenTextSplitter;

    public DocumentService(VectorStore vectorStore){
        this.vectorStore = vectorStore;
        this.tokenTextSplitter = new TokenTextSplitter();
    }

    public String processAndStoreDocument(MultipartFile file){
        String documentId = UUID.randomUUID().toString();
        Resource resource = file.getResource();

        // extract text from pdf
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource);
        List<Document> documents = pdfReader.get();
        List<Document> chunks = tokenTextSplitter.apply(documents);

        chunks.forEach(chunk -> {
            Map<String, Object> metadata = chunk.getMetadata();
            metadata.put("documentId", documentId);
            metadata.put("fileName", file.getOriginalFilename());
        });

        vectorStore.add(chunks);
        System.out.println("Processed " + chunks.size() + " chunks from " + file.getOriginalFilename());
        return documentId;
    }

    public List<Document> searchSimilarContent(String query, String documentId, int topK) {
        // Search for similar content within specific document
        return vectorStore.similaritySearch(SearchRequest
                .builder()
                        .query(query)
                        .topK(topK)
                        .similarityThreshold(0.7)
                        .filterExpression("metadata.documentId == '" + documentId + "'")
                .build()
        );
    }
    public void deleteDocument(String documentId) {
        // Note: This requires custom implementation as VectorStore doesn't have deleteByMetadata
        // For now, we'll skip this and implement later if needed
        System.out.println("Delete document feature not implemented yet");
    }

}
