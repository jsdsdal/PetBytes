package org.example.backend.pet.dto;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.pet.entity.PetSpecies;

public record CreatePetRequest(
        String name,
        PetSpecies species,
        PetPersonality personality
) {
}
