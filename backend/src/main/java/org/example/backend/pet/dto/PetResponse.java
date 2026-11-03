package org.example.backend.pet.dto;

import org.example.backend.pet.entity.PetLifeStage;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetSpecies;
import org.example.backend.personality.pet.PetPersonality;

public record PetResponse(
        Long id,
        String name,
        PetSpecies species,
        PetPersonality personality,
        PetLifeStage lifeStage,
        int hunger,
        int happiness,
        int energy
) {

    public static PetResponse from(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getPersonality(),
                pet.getLifeStage(),
                pet.getStats().getHungerLevel(),
                pet.getStats().getHappinessLevel(),
                pet.getStats().getEnergyLevel()
        );
    }
}