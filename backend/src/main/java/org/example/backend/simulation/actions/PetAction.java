package org.example.backend.simulation.actions;

import org.example.backend.interaction.PetInteractionService;
import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.pet.entity.Pet;



public interface PetAction {
    String name();

    double score(Pet pet, PersonalityProfile profile);

    void execute(Pet pet, PetActionLogService actions, boolean player);
}