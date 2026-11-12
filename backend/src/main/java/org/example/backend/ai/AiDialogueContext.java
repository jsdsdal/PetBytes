package org.example.backend.ai;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.pet.entity.PetSpecies;

import java.util.List;

public record AiDialogueContext(
        String petName,
        PetSpecies species,
        PetPersonality personality,
        double dialogueVerbosity,
        String dialogueStyle,
        int hunger,
        int happiness,
        int energy,
        List<String> recentEvents,
        List<String> previousMonologues // NEW: The pet's memory
) {}