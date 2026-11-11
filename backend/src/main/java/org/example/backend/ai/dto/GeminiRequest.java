package org.example.backend.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GeminiRequest(
        List<GeminiContent> contents,
        @JsonProperty("system_instruction") GeminiSystemInstruction systemInstruction
) {}