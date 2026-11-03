package org.example.backend.personality.pet;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonalityProfileRegistry {

    private final Map<PetPersonality, PersonalityProfile> profiles;

    public PersonalityProfileRegistry(
            List<PersonalityProfileProvider> providers) {

        this.profiles = providers.stream()
                .collect(Collectors.toMap(
                        PersonalityProfileProvider::personality,
                        PersonalityProfileProvider::profile
                ));
    }

    public PersonalityProfile getProfile(
            PetPersonality personality) {

        return profiles.get(personality);
    }
}
