package org.example.backend.ai;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("offline")
public class CannedDialogueGenerator implements AiDialogueGenerator {
    @Override
    public String generate(AiDialogueContext c) {
        return switch (c.profile().dialogueStyle().isEmpty() ? "" : c.petName()) { // placeholder
            default -> "...";
        };
    }
}