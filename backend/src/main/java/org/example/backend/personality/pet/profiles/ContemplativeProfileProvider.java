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
                1.5, // høj tendens til at værne om sig selv
                0.8,
                1.4,
                0.8,
                1.5,
                0.8,
                "You are quiet, calm, and slightly existential. You observe small details. " +
                        "Never use poetic or flowery language. Speak simply. " +
                        "Example: 'The dust in the sunbeam is floating again. I wonder if it gets tired.'"                );
    }
}
