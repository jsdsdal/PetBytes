package org.example.backend;


import org.example.backend.personality.pet.PersonalityProfileRegistry;
import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.personality.pet.profiles.ContemplativeProfileProvider;
import org.example.backend.personality.pet.profiles.EnergeticProfileProvider;
import org.example.backend.personality.user.UserPersonality;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetBond;
import org.example.backend.pet.entity.PetSpecies;
import org.example.backend.simulation.ActivityLogRepository;
import org.example.backend.simulation.AutonomousBehaviorService;
import org.example.backend.simulation.SimulationService;
import org.example.backend.simulation.bond.BondCalculator;
import org.example.backend.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class SimulationServiceTest {

    static final Instant T0 = Instant.parse("2026-11-06T08:00:00Z");

    final ActivityLogRepository logs = Mockito.mock(ActivityLogRepository.class);
    final BondCalculator bond = Mockito.mock(BondCalculator.class);
    final AutonomousBehaviorService autonomous = Mockito.mock(AutonomousBehaviorService.class);

    final SimulationService service = new SimulationService(
            new PersonalityProfileRegistry(List.of(
                    new EnergeticProfileProvider(),
                    new ContemplativeProfileProvider())),
            logs, bond, autonomous);

    @BeforeEach
    void setUp() {
        when(logs.findTop5ByPetIdOrderByOccurredAtDesc(any())).thenReturn(List.of());
        when(logs.countByPetId(any())).thenReturn(0L);
        when(bond.calculate(any(), any(), anyLong())).thenReturn(PetBond.STRANGER);
    }

    Pet petWith(PetPersonality personality) {
        User owner = new User("t", "t@t.dk", "x", UserPersonality.ANALYTICAL);
        return Pet.adopt("Test", PetSpecies.CAT, personality, false, owner, T0);
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