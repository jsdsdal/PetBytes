package org.example.backend.pet;

import org.example.backend.pet.dto.CreatePetRequest;
import org.example.backend.pet.dto.PetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse createPet(
            @RequestBody CreatePetRequest request
    ) {
        return petService.createPet(request);
    }

    @GetMapping("/{id}")
    public PetResponse getPet(@PathVariable Long id) {
        return petService.getPet(id);
    }
}