package org.example.backend.ai;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service @Profile("offline")
public class CannedDialogueGenerator implements AiDialogueGenerator {
    private final FallbackLines lines;
    public CannedDialogueGenerator(FallbackLines lines) { this.lines = lines; }

//    @Override
//    public String generate(AiDialogueContext c) { return lines.forPersonality(c.personality()); }
}