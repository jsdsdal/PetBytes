package org.example.backend.personality.pet.profiles;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileProvider;
import org.example.backend.personality.pet.PetPersonality;
import org.springframework.stereotype.Component;

@Component
public class EnergeticProfileProvider
        implements PersonalityProfileProvider {

    @Override
    public PetPersonality personality() {
        return PetPersonality.ENERGETIC;
    }

    @Override
    public PersonalityProfile profile() {
        return new PersonalityProfile(
                1.5,
                0.6,
                1.8,
                0.5,
                1.5,
                "Speaks in all caps. Chaotic and excitable."
        );
    }
}
