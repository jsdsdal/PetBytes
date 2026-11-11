package org.example.backend.interaction;

import org.example.backend.pet.PetRepository;
import org.example.backend.pet.PetStateService;
import org.example.backend.pet.dto.PetResponse;
import org.example.backend.pet.entity.Pet;
import org.example.backend.simulation.ActivityLog;
import org.example.backend.simulation.ActivityLogRepository;
import org.example.backend.simulation.ActivityType;
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
    private final ActivityLogRepository logs;
    private final Clock clock;

    @Transactional
    public PetResponse feed(Long petId, User user, int amount) {
        return interact(petId, user, ActivityType.FED, amount, pet -> pet.feed(amount));
    }

    @Transactional
    public PetResponse play(Long petId, User user, int amount) {
        return interact(petId, user, ActivityType.PLAYED, amount, pet -> pet.play(amount));
    }

    @Transactional
    public PetResponse sleep(Long petId, User user, int amount) {
        return interact(petId, user, ActivityType.NAPPED, amount, pet -> pet.sleep(amount));
    }

    private PetResponse interact(Long petId, User user, ActivityType type,
                                 int amount, Consumer<Pet> action) {
        Pet pet = state.getOwnedActive(petId, user);
        action.accept(pet);
        logs.save(new ActivityLog(pet, type, amount, clock.instant()));
        return PetResponse.from(pet);
    }
}