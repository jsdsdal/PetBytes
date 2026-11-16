package org.example.backend.personality.pet.profiles;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileProvider;
import org.example.backend.personality.pet.PetPersonality;
import org.springframework.stereotype.Component;

@Component
public class SpoiledProfileProvider
        implements PersonalityProfileProvider {

    @Override
    public PetPersonality personality() { return PetPersonality.SPOILED; }

    @Override
    public PersonalityProfile profile() {
        return new PersonalityProfile(
                0.4, // virkelig dårlig til at passe sig selv, ville hellere blive forkælet
                0.7,
                1.0,
                1.4,
                0.7,
                1.5,
                "You are a posh diva. You complain about minor inconveniences. " +
                        "You are obsessed with your appearance. You use short, snappy, snobby sentences. " +
                        "Example words to use: 'Hmph.' 'Ugh,' 'Seriously?' 'Don't click on me.'"
        );
    }
}
