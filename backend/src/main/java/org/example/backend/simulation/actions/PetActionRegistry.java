package org.example.backend.simulation.actions;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.pet.entity.Pet;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class PetActionRegistry {
    private final List<PetAction> actions;

    public PetActionRegistry(List<PetAction> actions) {
        this.actions = actions;
    }

    public PetAction best(Pet pet, PersonalityProfile profile) {
        return actions.stream()
                .max(Comparator.comparingDouble(a -> a.score(pet, profile)))
                .orElseThrow(() -> new IllegalStateException("No PetAction beans registered"));
    }
}
