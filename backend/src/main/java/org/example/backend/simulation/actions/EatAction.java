package org.example.backend.simulation.actions;

import org.example.backend.interaction.PetInteractionService;
import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.pet.entity.Pet;
import org.springframework.stereotype.Component;

@Component
public class EatAction implements PetAction {
    private static final int FEED_AMOUNT = 15;

    @Override public String name() { return "eat"; }

    @Override
    public double score(Pet pet, PersonalityProfile profile) {
        return pet.getStats().getHungerLevel() / 100.0;
    }

    @Override
    public void execute(Pet pet, PetActionLogService actions, boolean player) {
        actions.feed(pet, FEED_AMOUNT, player);
    }

}