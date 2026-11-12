package org.example.backend.adoption;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.personality.user.UserPersonality;
import org.springframework.stereotype.Service;

@Service
public class PersonalitySelectionService {
    private final PersonalityAffinityProvider affinity;
    private final WeightedPicker picker;

    public PersonalitySelectionService(PersonalityAffinityProvider affinity, WeightedPicker picker) {
        this.affinity = affinity;
        this.picker = picker;
    }

    public PetPersonality select(UserPersonality user) {
        return picker.pick(affinity.weightsFor(user));
    }
}