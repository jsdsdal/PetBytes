package org.example.backend.simulation;

import org.example.backend.interaction.PetInteractionService;
import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileRegistry;
import org.example.backend.pet.entity.Pet;
import org.example.backend.simulation.actions.PetAction;
import org.example.backend.simulation.actions.PetActionLogService;
import org.example.backend.simulation.actions.PetActionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
public class AutonomousBehaviorService {

    private static final Logger log = LoggerFactory.getLogger(AutonomousBehaviorService.class);

    private final PetActionRegistry registry;
    private final PersonalityProfileRegistry profiles;
    private final PetActionLogService actions;
    private final Random random;

    private static final double ACTIVATION_CHANCE = 0.35;

    public AutonomousBehaviorService(PetActionRegistry registry,
                                     PersonalityProfileRegistry profiles,
                                     PetActionLogService actions,
                                     Random random) {
        this.registry = registry;
        this.profiles = profiles;
        this.actions = actions;
        this.random = random;
    }

    public PetAction tick(Pet pet) {
        PersonalityProfile profile = profiles.getProfile(pet.getPersonality());
        PetAction best = registry.best(pet, profile);
        if (best.name().equals("idle") || random.nextDouble() > ACTIVATION_CHANCE * profile.selfCareTendency()) {
            return best;                // pet er idle
        }
        best.execute(pet, actions, false);
        return best;
    }

}