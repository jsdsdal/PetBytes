package org.example.backend.simulation.actions;

import org.example.backend.pet.entity.Pet;
import org.example.backend.simulation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class PetActionLogService {

    private final ActivityLogRepository logs;
    private final Clock clock;

    public PetActionLogService(ActivityLogRepository logs, Clock clock) {
        this.logs = logs;
        this.clock = clock;
    }

    @Transactional public void feed(Pet pet, int amount)  { pet.feed(amount);  log(pet, ActivityType.FED, amount); }
    @Transactional public void play(Pet pet, int amount)  { pet.play(amount);  log(pet, ActivityType.PLAYED, amount); }
    @Transactional public void sleep(Pet pet, int amount) { pet.sleep(amount); log(pet, ActivityType.NAPPED, amount); }

    private void log(Pet pet, ActivityType type, int amount) {
        logs.save(new ActivityLog(pet, type, amount, clock.instant()));
    }
}
