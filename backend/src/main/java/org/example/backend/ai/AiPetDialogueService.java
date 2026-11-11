package org.example.backend.ai;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.pet.entity.Pet;
import org.springframework.stereotype.Service;

@Service
public class AiPetDialogueService {

    private static final int RECENT_EVENTS = 5;


    // fields: petStateService, logRepository, generator, fallbackLines, profileRegistry

    public String talk(Long petId) {
        Pet pet = petStateService.getCurrent(petId);
        AiDialogueContext context = contextFor(pet);
        return generateOrFallback(context, pet.getPersonality());
    }

    private AiDialogueContext contextFor(Pet pet) {  } // bygger record for pet

    private String generateOrFallback(AiDialogueContext c, PetPersonality p) {
        try {
            return generator.generate(c);
        } catch (RuntimeException e) {
            log.warn("Dialogue generation failed, using fallback", e);
            return fallbackLines.forPersonality(p);
        }
    }
}