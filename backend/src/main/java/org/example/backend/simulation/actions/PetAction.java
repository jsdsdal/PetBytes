package org.example.backend.simulation.actions;

import org.example.backend.interaction.PetInteractionService;
import org.example.backend.pet.entity.Pet;

public interface PetAction {
    String name();
    double score(Pet pet);                    // 0.0 – 1.0
    void execute(Pet pet, PetInteractionService interactions);
}