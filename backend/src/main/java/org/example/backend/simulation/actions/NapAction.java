package org.example.backend.simulation.actions;

import org.example.backend.interaction.PetInteractionService;
import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.pet.entity.Pet;
import org.springframework.stereotype.Component;

@Component
public class NapAction implements PetAction {
    private static final int SLEEP_AMOUNT = 20;

    @Override public String name() { return "nap"; }

    @Override
    public double score(Pet pet, PersonalityProfile profile) {
        double deficit = (100 - pet.getStats().getEnergyLevel()) / 100.0;
        return deficit * profile.napTendency();
    }

    @Override
    public void execute(Pet pet, PetActionLogService actions) { actions.sleep(pet, SLEEP_AMOUNT); }
}