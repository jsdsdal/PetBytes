package org.example.backend.interaction;

import org.example.backend.pet.PetRepository;
import org.example.backend.pet.PetStateService;
import org.example.backend.pet.dto.PetResponse;
import org.example.backend.pet.entity.Pet;
import org.example.backend.simulation.ActivityLog;
import org.example.backend.simulation.ActivityLogRepository;
import org.example.backend.simulation.ActivityType;
import org.example.backend.simulation.actions.PetActionLogService;
import org.example.backend.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class PetInteractionService {

    private final PetStateService state;
    private final PetActionLogService actions;

    public PetInteractionService(PetStateService state, PetActionLogService actions) {
        this.state = state;
        this.actions = actions;
    }

    @Transactional
    public PetResponse feed(Long petId, User user, int amount) {
        Pet pet = state.getOwnedActive(petId, user);
        actions.feed(pet, amount, true);
        return PetResponse.from(pet);
    }

    @Transactional
    public PetResponse play(Long petId, User user, int amount) {
        Pet pet = state.getOwnedActive(petId, user);
        actions.play(pet, amount, true);
        return PetResponse.from(pet);
    }

    @Transactional
    public PetResponse sleep(Long petId, User user, int amount) {
        Pet pet = state.getOwnedActive(petId, user);
        actions.sleep(pet, amount, true);
        return PetResponse.from(pet);
    }
}