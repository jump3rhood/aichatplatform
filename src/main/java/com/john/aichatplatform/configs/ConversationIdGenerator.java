package com.john.aichatplatform.configs;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ConversationIdGenerator {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final Random random = new Random();

    public String generateId() {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return "conv_" + sb.toString();
    }
}
