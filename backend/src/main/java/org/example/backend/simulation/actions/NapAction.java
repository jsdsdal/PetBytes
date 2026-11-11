package org.example.backend.simulation.actions;

import org.example.backend.interaction.PetInteractionService;
import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.pet.entity.Pet;

public class NapAction implements PetAction {

    @Override
    public String name() { return "SLEEP"; }

    @Override
    public double score(Pet pet) {
        int energy = pet.getStats().getEnergyLevel();
        // lavere energi = højere utility
        double base = (100 - energy) / 100.0;

        // energetic gider helst ikke sove
        if (pet.getPersonality() == PetPersonality.ENERGETIC) {
            base *= 0.5;
        }
        return base;
    }

    @Override
    public void execute(Pet pet, PetInteractionService interactions) {
        interactions.sleep(pet.getId(), 20);
    }
}