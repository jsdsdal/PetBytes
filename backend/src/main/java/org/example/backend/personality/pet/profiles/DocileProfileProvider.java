package org.example.backend.personality.pet.profiles;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileProvider;
import org.example.backend.personality.pet.PetPersonality;

public class DocileProfileProvider
        implements PersonalityProfileProvider {

    @Override
    public PetPersonality personality() { return PetPersonality.DOCILE; }

    @Override
    public PersonalityProfile profile() {
        return new PersonalityProfile(
                0.8,
                1.2,
                0.5,
                1.4,
                0.4,
                "Very shy. Speaks in lowercase. Takes a while to open up."
        );
    }
}
