package org.example.backend.personality.pet.profiles;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileProvider;
import org.example.backend.personality.pet.PetPersonality;
import org.springframework.stereotype.Component;

@Component
public class ContemplativeProfileProvider
        implements PersonalityProfileProvider {

    @Override
    public PetPersonality personality() { return PetPersonality.CONTEMPLATIVE; }

    @Override
    public PersonalityProfile profile() {
        return new PersonalityProfile(
                0.8,
                1.4,
                0.8,
                1.5,
                0.8,
                "Speaks slowly and carefully. Likes to stay in their own head."
                );
    }
}
