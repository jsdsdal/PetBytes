package org.example.backend.pet;

import org.example.backend.pet.entity.Pet;
import org.example.backend.simulation.SimulationService;
import org.example.backend.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;

@Service
public class PetStateService {

    private final PetRepository pets;
    private final SimulationService simulation;
    private final Clock clock;

    public PetStateService(PetRepository pets, SimulationService simulation, Clock clock) {
        this.pets = pets;
        this.simulation = simulation;
        this.clock = clock;
    }

    public Pet findOwned(Long petId, User owner) {       // 404 for missing OR someone else's
        return pets.findByIdAndOwnerId(petId, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
    }

    @Transactional
    public Pet getOwned(Long petId, User owner) {
        return refreshed(findOwned(petId, owner));
    }

    @Transactional
    public Pet getOwnedActive(Long petId, User owner) {
        Pet pet = getOwned(petId, owner);
        if (!pet.isActive()) throw conflict("Pet is not active");
        return pet;
    }

    public Pet refreshed(Pet pet) {
        if (pet.isActive()) simulation.materialize(pet, clock.instant());
        return pet;
    }

    static ResponseStatusException conflict(String message) {
        return new ResponseStatusException(HttpStatus.CONFLICT, message);
    }
}