package org.example.backend.ai.dto;

import java.util.List;

public record GeminiContent(String role, List<GeminiPart> parts) {
    public GeminiContent(String role, String text) { this(role, List.of(new GeminiPart(text))); }
}