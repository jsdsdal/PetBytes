package org.example.backend.ai;

import org.springframework.stereotype.Service;

@Service
public class CannedDialogueGenerator implements AiDialogueGenerator {

    private final FallbackLines lines;

    public CannedDialogueGenerator(FallbackLines lines) {
        this.lines = lines;
    }

    @Override
    public String generate(AiDialogueContext context) {
        return lines.forPersonality(context.personality());
    }
}