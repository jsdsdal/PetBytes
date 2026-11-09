package org.example.backend.interaction;

import org.example.backend.pet.PetRepository;
import org.example.backend.pet.dto.PetResponse;
import org.example.backend.pet.entity.Pet;
import org.example.backend.simulation.ActivityLog;
import org.example.backend.simulation.ActivityLogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class PetInteractionService {

    private final PetRepository petRepository;
    private final ActivityLogRepository logRepository;

    public PetInteractionService(PetRepository petRepository, ActivityLogRepository logRepository) {
        this.petRepository = petRepository;
        this.logRepository = logRepository;
    }

    @Transactional
    public PetResponse feed(Long petId, int amount) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        pet.feed(amount);
        logRepository.save(new ActivityLog(pet, "FED", Map.of("amount", amount)));

        return PetResponse.from(pet); // dirty checking
    }

    @Transactional
    public PetResponse play(Long petId, int amount) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        pet.play(amount);
        logRepository.save(new ActivityLog(pet, "PLAYED", Map.of("amount", amount)));

        return PetResponse.from(pet);
    }

    @Transactional
    public PetResponse sleep(Long petId, int amount) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        pet.sleep(amount);
        logRepository.save(new ActivityLog(pet, "SLEPT", Map.of("amount", amount)));

        return PetResponse.from(pet);
    }
}