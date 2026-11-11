package org.example.backend.adoption;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.personality.user.UserPersonality;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class PersonalityAffinityProvider {

    private record Affinity(PetPersonality primary, PetPersonality secondary) {}

    private static final int PRIMARY = 55, SECONDARY = 25, OTHER = 10;

    private static final Map<UserPersonality, Affinity> AFFINITIES = Map.of(
            UserPersonality.ANALYTICAL,  new Affinity(PetPersonality.CONTEMPLATIVE, PetPersonality.DOCILE),
            UserPersonality.CAREGIVER,   new Affinity(PetPersonality.DOCILE, PetPersonality.SPOILED),
            UserPersonality.SPONTANEOUS, new Affinity(PetPersonality.ENERGETIC, PetPersonality.SPOILED),
            UserPersonality.METICULOUS,  new Affinity(PetPersonality.CONTEMPLATIVE, PetPersonality.ENERGETIC));

    public Map<PetPersonality, Integer> weightsFor(UserPersonality user) {
        Affinity affinity = AFFINITIES.get(user);
        Map<PetPersonality, Integer> weights = new EnumMap<>(PetPersonality.class);
        for (PetPersonality p : PetPersonality.values()) {
            weights.put(p, weightOf(p, affinity));
        }
        return weights;
    }

    private static int weightOf(PetPersonality p, Affinity a) {
        if (p == a.primary()) return PRIMARY;
        if (p == a.secondary()) return SECONDARY;
        return OTHER;
    }
}