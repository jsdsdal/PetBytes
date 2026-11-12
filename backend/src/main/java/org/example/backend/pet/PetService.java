package org.example.backend.pet;

import org.example.backend.pet.dto.PetResponse;
import org.example.backend.pet.entity.Pet;
import org.example.backend.pet.entity.PetStatus;
import org.example.backend.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.util.List;

@Service
public class PetService {

    private final PetRepository pets;
    private final PetStateService state;
    private final Clock clock;

    public PetService(PetRepository pets, PetStateService state, Clock clock) {
        this.pets = pets;
        this.state = state;
        this.clock = clock;
    }

    @Transactional
    public List<PetResponse> listMine(User user) {
        return pets.findByOwnerIdOrderByBornAtDesc(user.getId()).stream()
                .map(state::refreshed).map(PetResponse::from).toList();
    }

    @Transactional
    public PetResponse getPet(Long id, User user) {
        return PetResponse.from(state.getOwned(id, user));
    }

    @Transactional
    public PetResponse shelve(Long id, User user) {
        Pet pet = state.getOwnedActive(id, user);       // applies elapsed time, then freezes
        pet.shelve();
        return PetResponse.from(pet);
    }

    @Transactional
    public PetResponse activate(Long id, User user) {
        Pet pet = state.getOwned(id, user);
        requireStatus(pet, PetStatus.INACTIVE);
        requireNoActivePet(user);
        pet.reactivate(clock.instant());
        return PetResponse.from(pet);
    }

    private void requireStatus(Pet pet, PetStatus expected) {
        if (pet.getStatus() != expected) throw PetStateService.conflict("Pet is " + pet.getStatus());
    }

    private void requireNoActivePet(User user) {
        if (pets.existsByOwnerIdAndStatus(user.getId(), PetStatus.ACTIVE)) {
            throw PetStateService.conflict("Shelve your current pet first");
        }
    }
}