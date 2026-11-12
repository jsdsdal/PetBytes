package org.example.backend.simulation.bond;

import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetBond;
import org.example.backend.simulation.ActivityLog;
import org.example.backend.simulation.ActivityType;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;


/**
Udregner spillerens bond til sit pet ud fra observerbar data gemt i loggen.
 Ikke testet.
 */

@Service
public final class BondCalculator {

    // vægte, skal tunes
    private static final double DAYS_WEIGHT = 0.3;
    private static final double TOTAL_INTERACTIONS_WEIGHT = 1.5;
    private static final double RECENT_INTERACTIONS_WEIGHT = 3.0;
    private static final double VARIETY_WEIGHT = 5.0;
    private static final int RECENT_DAYS = 7;

    public PetBond calculate(Pet pet, List<ActivityLog> allRecent, long totalInteractions) {
        long daysAlive = Duration.between(pet.getBornAt(), Instant.now()).toDays();

        Instant cutoff = Instant.now().minus(Duration.ofDays(RECENT_DAYS));
        long recentInteractions = allRecent.stream()
                .filter(a -> a.getOccurredAt().isAfter(cutoff))
                .count();

        long variety = allRecent.stream()
                .map(ActivityLog::getType)
                .distinct()
                .count();

        double score = (daysAlive * DAYS_WEIGHT)
                + (totalInteractions * TOTAL_INTERACTIONS_WEIGHT)
                + (recentInteractions * RECENT_INTERACTIONS_WEIGHT)
                + (variety * VARIETY_WEIGHT);

        return tierFor(score);
    }

    private PetBond tierFor(double score) {
        if (score < 15)  return PetBond.STRANGER;
        if (score < 40)  return PetBond.ACQUAINTANCE;
        if (score < 80)  return PetBond.COMPANION;
        if (score < 140) return PetBond.FRIEND;
        return PetBond.BONDED;
    }
}
