package org.example.backend.ai;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.pet.entity.PetSpecies;

import java.util.List;

public record AiDialogueContext(
        String petName, PetSpecies species, PersonalityProfile profile,
        int hunger, int happiness, int energy, List<String> recentEvents) {}