package org.example.backend.simulation.actions;

import org.example.backend.interaction.PetInteractionService;
import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.pet.entity.Pet;
import org.springframework.stereotype.Component;

@Component
public class EatAction implements PetAction {
    private static final int PLAYER_FEED = 15;
    private static final int SELF_FEED = 3;
    private static final int SELF_FEED_FLOOR = 20;

    @Override public String name() { return "eat"; }

    @Override
    public double score(Pet pet, PersonalityProfile profile) {
        return pet.getStats().getHungerLevel() / 100.0;
    }

    @Override
    public void execute(Pet pet, PetActionLogService actions, boolean player) {
        if (!player && pet.getStats().getHungerLevel() <= SELF_FEED_FLOOR) return;
        actions.feed(pet, player ? PLAYER_FEED : SELF_FEED, player);
    }

}