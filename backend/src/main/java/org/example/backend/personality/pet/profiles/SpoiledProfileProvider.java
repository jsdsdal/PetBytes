package org.example.backend.personality.pet.profiles;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileProvider;
import org.example.backend.personality.pet.PetPersonality;

public class SpoiledProfileProvider
        implements PersonalityProfileProvider {

    @Override
    public PetPersonality personality() { return PetPersonality.SPOILED; }

    @Override
    public PersonalityProfile profile() {
        return new PersonalityProfile(
                0.7,
                1.0,
                1.4,
                0.7,
                2.0,
                "Thinks very highly of themselves. Speaks in dramatic flair and prose. Flamboyant and dramatic."
        );
    }
}
