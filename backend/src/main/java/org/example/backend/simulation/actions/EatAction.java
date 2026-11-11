package org.example.backend.simulation.actions;

import org.example.backend.interaction.PetInteractionService;
import org.example.backend.pet.entity.Pet;
import org.springframework.stereotype.Component;

@Component
public class EatAction implements PetAction {

    @Override
    public String name() { return "EAT"; }

    @Override
    public double score(Pet pet) {
        int hunger = pet.getStats().getHungerLevel();
        // højere hunger → højere utility. normaliseres til 0..1.
        return hunger / 100.0;
    }

    @Override
    public void execute(Pet pet, PetInteractionService interactions) {
        interactions.feed(pet.getId(), 20);
    }
}
