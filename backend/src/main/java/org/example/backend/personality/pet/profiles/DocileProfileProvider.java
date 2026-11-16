package org.example.backend.personality.pet.profiles;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileProvider;
import org.example.backend.personality.pet.PetPersonality;
import org.springframework.stereotype.Component;

@Component
public class DocileProfileProvider
        implements PersonalityProfileProvider {

    @Override
    public PetPersonality personality() { return PetPersonality.DOCILE; }

    @Override
    public PersonalityProfile profile() {
        return new PersonalityProfile(
                1.0,
                0.8,
                1.2,
                0.5,
                1.4,
                0.4,
                "You are very shy, nervous, and quiet. You speak in lowercase. " +
                        "You stutter or trail off. Never apologize more than once. Keep it very short. " +
                        "Example: 'oh... um. i'm just going to sit over here, if that's okay.'"        );
    }
}
