package org.example.backend.ai;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "dialogue.provider", havingValue = "scripted", matchIfMissing = true)
public class CannedDialogueGenerator implements AiDialogueGenerator {
    private final FallbackLines lines;
    public CannedDialogueGenerator(FallbackLines lines) { this.lines = lines; }

//    @Override
//    public String generate(AiDialogueContext c) { return lines.forPersonality(c.personality()); }
}