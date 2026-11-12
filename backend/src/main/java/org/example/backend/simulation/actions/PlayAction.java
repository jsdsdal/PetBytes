package org.example.backend.simulation.actions;

import org.example.backend.interaction.PetInteractionService;
import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.pet.entity.Pet;
import org.springframework.stereotype.Component;

@Component
public class PlayAction implements PetAction {
    private static final int PLAY_AMOUNT = 15;

    @Override public String name() { return "play"; }

    @Override
    public double score(Pet pet, PersonalityProfile profile) {
        double deficit = (100 - pet.getStats().getHappinessLevel()) / 100.0;
        return deficit * profile.playTendency();
    }

    @Override
    public void execute(Pet pet, PetActionLogService actions, boolean player) { actions.play(pet, PLAY_AMOUNT, player); }
}