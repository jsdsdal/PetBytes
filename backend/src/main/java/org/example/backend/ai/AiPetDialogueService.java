package org.example.backend.ai;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileRegistry;
import org.example.backend.pet.PetStateService;
import org.example.backend.pet.entity.Pet;
import org.example.backend.simulation.ActivityResponse;
import org.example.backend.simulation.ActivityService;
import org.example.backend.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AiPetDialogueService {

    private static final Logger log = LoggerFactory.getLogger(AiPetDialogueService.class);

    private final PetStateService petStateService;
    private final ActivityService activityService;
    private final PersonalityProfileRegistry profileRegistry;
    private final AiDialogueGenerator primaryGenerator;
    private final CannedDialogueGenerator fallbackGenerator;
    private final AiMonologueRepository monologueRepository;

    public AiPetDialogueService(
            PetStateService petStateService,
            ActivityService activityService,
            PersonalityProfileRegistry profileRegistry,
            AiDialogueGenerator primaryGenerator,
            CannedDialogueGenerator fallbackGenerator,
            AiMonologueRepository monologueRepository) {
        this.petStateService = petStateService;
        this.activityService = activityService;
        this.profileRegistry = profileRegistry;
        this.primaryGenerator = primaryGenerator;
        this.fallbackGenerator = fallbackGenerator;
        this.monologueRepository = monologueRepository;
    }

    @Transactional // gemmer entity
    public String generateMonologue(Long petId, User owner) {
        Pet pet = petStateService.getOwned(petId, owner);
        AiDialogueContext context = contextFor(pet, owner);

        // 1. generate text
        String dialogue = generateWithFallback(context);

        // 2. save to memory
        monologueRepository.save(new AiMonologue(pet, dialogue));

        // 3. return to frontend
        return dialogue;
    }

    private AiDialogueContext contextFor(Pet pet, User owner) {
        PersonalityProfile profile = profileRegistry.getProfile(pet.getPersonality());

        List<String> recentActivity = activityService
                .recent(pet.getId(), owner, 5)
                .stream()
                .map(ActivityResponse::description)
                .toList();

        //  Fetch sidste 5 ting den har sagt
        List<String> previousMonologues = monologueRepository
                .findTop5ByPetIdOrderBySaidAtDesc(pet.getId())
                .stream()
                .map(AiMonologue::getText)
                .toList();

        return new AiDialogueContext(
                pet.getName(),
                pet.getSpecies(),
                pet.getPersonality(),
                profile.dialogueVerbosity(),
                profile.dialogueStyle(),
                pet.getStats().getHungerLevel(),
                pet.getStats().getHappinessLevel(),
                pet.getStats().getEnergyLevel(),
                recentActivity,
                previousMonologues
        );
    }

    private String generateWithFallback(AiDialogueContext context) {
        try {
            String result = primaryGenerator.generate(context);
            if (result != null && !result.isBlank()) {
                return result;
            }
            log.warn("Primary dialogue generator returned blank; using fallback");
        } catch (RuntimeException e) {
            log.warn("Primary dialogue generator failed; using fallback", e);
        }
        return fallbackGenerator.generate(context);
    }
}