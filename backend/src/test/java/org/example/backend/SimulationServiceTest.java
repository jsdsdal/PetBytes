package org.example.backend;


import org.example.backend.personality.pet.PersonalityProfileRegistry;
import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.personality.pet.profiles.ContemplativeProfileProvider;
import org.example.backend.personality.pet.profiles.EnergeticProfileProvider;
import org.example.backend.personality.user.UserPersonality;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetBond;
import org.example.backend.pet.entity.PetSpecies;
import org.example.backend.simulation.SimulationService;
import org.example.backend.user.User;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SimulationServiceTest {
    static final Instant T0 = Instant.parse("2026-11-06T08:00:00Z");
    final SimulationService service = new SimulationService(new PersonalityProfileRegistry(
            List.of(new EnergeticProfileProvider(), new ContemplativeProfileProvider())));

    Pet petWith(PetPersonality personality) {
        User owner = new User("t", "t@t.dk", "x", UserPersonality.ANALYTICAL);
        Pet pet = new Pet("Test", PetSpecies.CAT, false, personality, PetBond.STRANGER, owner);
        pet.setLastSimulatedAt(T0);
        return pet;
    }

    @Test
    void energeticGetsHungrierThanContemplative() {
        Pet wild = petWith(PetPersonality.ENERGETIC), calm = petWith(PetPersonality.CONTEMPLATIVE);
        service.materialize(wild, T0.plus(Duration.ofHours(6)));
        service.materialize(calm, T0.plus(Duration.ofHours(6)));
        assertThat(wild.getStats().getHungerLevel()).isEqualTo(95); // 50 + 7.5*6
        assertThat(calm.getStats().getHungerLevel()).isEqualTo(74); // 50 + 4*6
    }

    @Test void statsClampAfterLongNeglect() {
        Pet pet = petWith(PetPersonality.ENERGETIC);
        service.materialize(pet, T0.plus(Duration.ofDays(2)));
        assertThat(pet.getStats().getHungerLevel()).isEqualTo(100);
        assertThat(pet.getStats().getEnergyLevel()).isZero();
    }

    @Test void shortGapChangesNothingAndKeepsTimestamp() {
        Pet pet = petWith(PetPersonality.ENERGETIC);
        service.materialize(pet, T0.plusSeconds(120));
        assertThat(pet.getLastSimulatedAt()).isEqualTo(T0);
    }
}