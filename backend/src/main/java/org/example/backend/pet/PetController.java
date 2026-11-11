package org.example.backend.pet;

import org.example.backend.pet.dto.PetResponse;
import org.example.backend.pet.entity.Pet;
import org.example.backend.user.CurrentUser;
import org.example.backend.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;
    private final CurrentUser currentUser;

    public PetController(PetService petService, CurrentUser currentUser) {
        this.petService = petService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public List<PetResponse> myPets(Principal principal) {
        return petService.listMine(currentUser.from(principal));
    }

    @GetMapping("/{id}")
    public PetResponse getPet(@PathVariable Long id, Principal principal) {
        return petService.getPet(id, currentUser.from(principal));
    }

    @PostMapping("/{id}/shelve")
    public PetResponse shelve(@PathVariable Long id, Principal principal) {
        return petService.shelve(id, currentUser.from(principal));
    }

    @PostMapping("/{id}/activate")
    public PetResponse activate(@PathVariable Long id, Principal principal) {
        return petService.activate(id, currentUser.from(principal));
    }



}