package org.example.backend.personality.pet;

public record PersonalityProfile(
        double hungerRate,
        double energyDepletionRate,
        double playTendency,
        double napTendency,
        double dialogueVerbosity,
        String dialogueStyle
) {
}
