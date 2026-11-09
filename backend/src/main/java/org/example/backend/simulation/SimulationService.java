package org.example.backend.simulation;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileRegistry;
import org.example.backend.pet.entity.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;


// O(1) pr request
@Service
public class SimulationService {

    private final PersonalityProfileRegistry profileRegistry;

    public SimulationService(PersonalityProfileRegistry profileRegistry) {
        this.profileRegistry = profileRegistry;
    }

    @Transactional
    public void materialize(Pet pet) {
        Duration elapsed = Duration.between(pet.getLastSimulatedAt(), Instant.now());
        double hours = elapsed.toMinutes() / 60.0;

        if (hours < 0.01) return; // ligegyldigt at simulere, skal justeres

        // base decay rates (pr time)
        double hungerRate = 5.0;
        double energyRate = 3.0;

        // Personality modifiers
        PersonalityProfile profile = profileRegistry.getProfile(pet.getPersonality());
        hungerRate *= profile.hungerRate();
        energyRate *= profile.energyDepletionRate();

        pet.getStats().addHunger((int) (hungerRate * hours));
        pet.getStats().addEnergy((int) (-energyRate * hours));

        pet.setLastSimulatedAt(Instant.now());
    }
}