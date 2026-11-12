package org.example.backend.ai;

import org.example.backend.personality.pet.PetPersonality;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FallbackLines {
    private static final Map<PetPersonality, String> LINES = Map.of(
            PetPersonality.ENERGETIC, "HI HI HI!! WHAT ARE WE DOING?!",
            PetPersonality.DOCILE, "...hi. i'm here.",
            PetPersonality.SPOILED, "Darling, do try to keep up.",
            PetPersonality.CONTEMPLATIVE, "I was just thinking... about nothing, really.");

    public String forPersonality(PetPersonality p) { return LINES.get(p); }
}