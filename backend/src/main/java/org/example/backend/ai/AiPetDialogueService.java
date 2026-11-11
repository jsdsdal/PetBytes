package org.example.backend.ai;

import org.example.backend.personality.pet.PersonalityProfile;
import org.example.backend.personality.pet.PersonalityProfileRegistry;
import org.example.backend.pet.entity.Pet;
import org.example.backend.simulation.ActivityLog;
import org.example.backend.simulation.ActivityLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AiPetDialogueService {

    private static final Logger log = LoggerFactory.getLogger(AiPetDialogueService.class);

    private final AiDialogueGenerator primaryGenerator;
    private final AiDialogueGenerator fallbackGenerator;
    private final ActivityLogRepository activityLogRepository;
    private final AiMonologueRepository aiMonologueRepository;
    private final PersonalityProfileRegistry profileRegistry;

    public AiPetDialogueService(
            GeminiDialogueGenerator primaryGenerator,
            CannedDialogueGenerator fallbackGenerator,
            ActivityLogRepository activityLogRepository,
            AiMonologueRepository aiMonologueRepository,
            PersonalityProfileRegistry profileRegistry) {
        this.primaryGenerator = primaryGenerator;
        this.fallbackGenerator = fallbackGenerator;
        this.activityLogRepository = activityLogRepository;
        this.aiMonologueRepository = aiMonologueRepository;
        this.profileRegistry = profileRegistry;
    }

    public String generateAndPersist(Pet pet) {
        AiDialogueContext context = contextFor(pet);
        String monologue = invokeWithFallback(context);
        aiMonologueRepository.save(new AiMonologue(pet, monologue));
        return monologue;
    }

    AiDialogueContext contextFor(Pet pet) {
        PersonalityProfile profile = profileRegistry.getProfile(pet.getPersonality());

        List<ActivityLog> recentActivity = activityLogRepository
                .findTop5ByPetIdOrderByOccurredAtDesc(pet.getId());
        List<String> recentMonologues = aiMonologueRepository
                .findTop5ByPetIdOrderBySaidAtDesc(pet.getId())
                .stream()
                .map(AiMonologue::getText)
                .toList();

        long daysAlive = Duration.between(pet.getBornAt(), Instant.now()).toDays();

        return new AiDialogueContext(
                pet.getName(),
                pet.getSpecies().name(),
                pet.getPersonality().name(),
                profile.dialogueStyle(),
                pet.getLifeStage().name(),
                pet.getBond(),              // ← read from pet, not calculated here
                daysAlive,
                recentActivity.stream().map(this::summarize).toList(),
                recentMonologues,
                pet.getStats().getHungerLevel(),
                pet.getStats().getHappinessLevel(),
                pet.getStats().getEnergyLevel()
        );
    }

    private String invokeWithFallback(AiDialogueContext context) {
        try {
            return primaryGenerator.generate(context);
        } catch (Exception e) {
            log.warn("Primary AI generation failed, using canned fallback: {}", e.getMessage());
            return fallbackGenerator.generate(context);
        }
    }

    private String summarize(ActivityLog log) {
        String base = log.getType().name().toLowerCase().replace('_', ' ');
        return log.getAmount() != null
                ? base + " (amount " + log.getAmount() + ") at " + log.getOccurredAt()
                : base + " at " + log.getOccurredAt();
    }
}