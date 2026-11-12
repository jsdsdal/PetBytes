package org.example.backend.simulation.actions;

import org.example.backend.interaction.PetInteractionService;
import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.pet.entity.Pet;
import org.springframework.stereotype.Component;

@Component
public class IdleAction implements PetAction {
    private static final double BASE_SCORE = 0.05;

    @Override public String name() { return "idle"; }

    @Override
    public double score(Pet pet, PersonalityProfile profile) {
        return BASE_SCORE;
    }

    @Override
    public void execute(Pet pet, PetActionLogService actions, boolean player) {
        // tom med vilje - skal bare glo ud i luften
    }
}
