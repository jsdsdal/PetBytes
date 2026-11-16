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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AiPetDialogueService {

    private static final Logger log = LoggerFactory.getLogger(AiPetDialogueService.class);

    private final PetStateService petStateService;
    private final ActivityService activityService;
    private final PersonalityProfileRegistry profileRegistry;

    private final AiDialogueGenerator geminiGenerator;
    private final AiDialogueGenerator openAiGenerator;
    private final CannedDialogueGenerator cannedGenerator;

    private final String primaryProvider;

    private final AiMonologueRepository monologueRepository;
    private final DialogueTopicBank topics;
    private final DialogueMoodBank moods;

    public AiPetDialogueService(
            PetStateService petStateService,
            ActivityService activityService,
            PersonalityProfileRegistry profileRegistry,
            @Qualifier("geminiDialogueGenerator") AiDialogueGenerator geminiGenerator,
            @Qualifier("openAiDialogueGenerator") AiDialogueGenerator openAiGenerator,
            CannedDialogueGenerator cannedGenerator,
            @Value("${dialogue.provider:gemini}") String primaryProvider,
            AiMonologueRepository monologueRepository,
            DialogueTopicBank topics,
            DialogueMoodBank moods) {
        this.petStateService = petStateService;
        this.activityService = activityService;
        this.profileRegistry = profileRegistry;
        this.geminiGenerator = geminiGenerator;
        this.openAiGenerator = openAiGenerator;
        this.cannedGenerator = cannedGenerator;
        this.primaryProvider = primaryProvider;
        this.monologueRepository = monologueRepository;
        this.topics = topics;
        this.moods = moods;
    }

    @Transactional
    public String generateMonologue(Long petId, User owner) {
        Pet pet = petStateService.getOwned(petId, owner);
        AiDialogueContext context = contextFor(pet, owner);

        String dialogue = null;

        // baseret på app props -> vælger gemini først, så openai
        if ("gemini".equalsIgnoreCase(primaryProvider)) {
            dialogue = tryGenerate(geminiGenerator, context, "Gemini");
            if (dialogue == null) dialogue = tryGenerate(openAiGenerator, context, "OpenRouter");
        } else {
            dialogue = tryGenerate(openAiGenerator, context, "OpenRouter");
            if (dialogue == null) dialogue = tryGenerate(geminiGenerator, context, "Gemini");
        }

        // fallback to canned
        if (dialogue == null) {
            log.warn("Both AI providers failed; using canned dialogue");
            dialogue = cannedGenerator.generate(context);
        }

        monologueRepository.save(new AiMonologue(pet, dialogue));
        return dialogue;
    }

    private String tryGenerate(AiDialogueGenerator generator, AiDialogueContext context, String providerName) {
        try {
            String result = generator.generate(context);
            if (result != null && !result.isBlank()) {
                // for at se hvem der lykkes
                log.info("{} successfully generated a monologue.", providerName);
                return result;
            }
            log.warn("{} returned a blank response", providerName);
        } catch (Exception e) {
            log.error("{} failed to generate dialogue", providerName, e);
        }
        return null;
    }

    private AiDialogueContext contextFor(Pet pet, User owner) {
        PersonalityProfile profile = profileRegistry.getProfile(pet.getPersonality());

        List<String> recentActivity = activityService
                .recent(pet.getId(), owner, 5)
                .stream()
                .map(ActivityResponse::description)
                .toList();

        List<String> previousMonologues = monologueRepository
                .findTop5ByPetIdOrderBySaidAtDesc(pet.getId())
                .stream()
                .map(AiMonologue::getText)
                .toList();

        String topicSeed = topics.pick();
        String moodSeed = moods.pick();

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
                previousMonologues,
                topicSeed,
                moodSeed,
                pet.getPetBond()
        );
    }
}