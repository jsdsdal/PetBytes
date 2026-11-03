package org.example.backend.pet;

import org.example.backend.pet.dto.CreatePetRequest;
import org.example.backend.pet.dto.PetResponse;
import org.example.backend.pet.entity.Pet;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public PetResponse createPet(CreatePetRequest request) {

        Pet pet = new Pet(
                request.name(),
                request.species(),
                request.personality()
        );

        Pet savedPet = petRepository.save(pet);

        return PetResponse.from(savedPet);
    }

    public PetResponse getPet(Long id) {

        Pet pet = petRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Pet not found"
                        )
                );

        return PetResponse.from(pet);
    }
}