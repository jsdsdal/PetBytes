package org.example.backend.pet.dto;

import org.example.backend.pet.entity.*;
import org.example.backend.personality.pet.PetPersonality;

public record PetResponse(
        Long id, String name, PetSpecies species, PetPersonality personality,
        boolean variant, PetBond petBond, PetLifeStage lifeStage, PetStatus status,
        int hunger, int happiness, int energy) {

    public static PetResponse from(Pet pet) {
        PetStats s = pet.getStats();
        return new PetResponse(pet.getId(), pet.getName(), pet.getSpecies(), pet.getPersonality(),
                pet.isVariant(), pet.getPetBond(), pet.getLifeStage(), pet.getStatus(),
                s.getHungerLevel(), s.getHappinessLevel(), s.getEnergyLevel());
    }
}