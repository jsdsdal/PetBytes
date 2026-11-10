package org.example.backend.pet;

import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetSpecies;
import org.example.backend.simulation.ActivityLog;
import org.example.backend.simulation.ActivityLogRepository;
import org.example.backend.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public class AdoptionService {

    private final PetRepository petRepository;
    private final ActivityLogRepository activityLogRepository;

    public AdoptionService(PetRepository petRepository, ActivityLogRepository activityLogRepository) {
        this.petRepository = petRepository;
        this.activityLogRepository = activityLogRepository;
    }

//    @Transactional
//    public Pet adopt(User user) {
//        PetPersonality personality = personalitySelector.select(user.getPersonality());
//        PetSpecies species = speciesSelector.select(personality);
//        boolean unique = random.nextDouble() < petUniqueRate;
//        Pet pet = new Pet("Unnamed", species, personality, unique, user);
//        petRepository.save(pet);
//        activityLogRepository.save(new ActivityLog(pet, "ADOPTED", Map.of("unique", unique)));
//        return pet;
//    }

}


