package org.example.backend.simulation;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileRegistry;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetBond;
import org.example.backend.simulation.bond.BondCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;


// O(1) pr request
@Service
public class SimulationService {

    private static final Duration MIN_STEP = Duration.ofMinutes(10);
    private static final Duration TICK_INTERVAL = Duration.ofHours(2);  // one autonomous action per ~2h away
    private static final int MAX_CATCHUP_TICKS = 3;                     // hard cap
    private static final double BASE_HUNGER = 5.0;
    private static final double BASE_ENERGY = 3.0;

    private final PersonalityProfileRegistry profileRegistry;
    private final ActivityLogRepository activityLogRepository;
    private final BondCalculator bondCalculator;
    private final AutonomousBehaviorService autonomousBehavior;

    public SimulationService(PersonalityProfileRegistry profileRegistry,
                             ActivityLogRepository activityLogRepository,
                             BondCalculator bondCalculator,
                             AutonomousBehaviorService autonomousBehavior) {
        this.profileRegistry = profileRegistry;
        this.activityLogRepository = activityLogRepository;
        this.bondCalculator = bondCalculator;
        this.autonomousBehavior = autonomousBehavior;
    }

    @Transactional
    public void materialize(Pet pet, Instant now) {
        Duration elapsed = Duration.between(pet.getLastSimulatedAt(), now);
        if (elapsed.compareTo(MIN_STEP) < 0) return;

        // 1. Decay + bond (pure, testable)
        double hours = elapsed.toSeconds() / 3600.0;
        PersonalityProfile p = profileRegistry.getProfile(pet.getPersonality());
        pet.getStats().addHunger(decay(BASE_HUNGER * p.hungerRate(), hours));
        pet.getStats().addEnergy(-decay(BASE_ENERGY * p.energyDepletionRate(), hours));
        pet.markSimulated(now);

        List<ActivityLog> recent = activityLogRepository
                .findTop5ByPetIdOrderByOccurredAtDesc(pet.getId());
        long total = activityLogRepository.countByPetId(pet.getId());
        pet.setPetBond(bondCalculator.calculate(pet, recent, total));

        // 2. Autonomous catch-up (bounded, so absence doesn't run forever)
        int ticks = (int) Math.min(elapsed.dividedBy(TICK_INTERVAL), MAX_CATCHUP_TICKS);
        for (int i = 0; i < ticks; i++) {
            autonomousBehavior.tick(pet);
        }
    }

    private static int decay(double perHour, double hours) {
        return (int) Math.round(perHour * hours);
    }
}