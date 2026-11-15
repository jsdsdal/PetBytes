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
                0.7,
                1.5,
                0.6,
                1.8,
                0.5,
                1.5,
                "You are easily distracted and hyperactive. You speak in short bursts, " +
                        "interrupt yourself, and change topics quickly. Do not use all caps constantly, " +
                        "just speak fast. Example: 'OH! A bug! Wait, what was I saying? Oh right, food!'"        );
    }
}
