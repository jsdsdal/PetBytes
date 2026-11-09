package org.example.backend.interaction;

import org.example.backend.pet.dto.PetResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pets/{petId}")
public class PetInteractionController {

    private final PetInteractionService interactionService;

    public PetInteractionController(PetInteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @PostMapping("/feed")
    public PetResponse feed(@PathVariable Long petId,
                            @RequestBody(required = false) InteractionRequest request) {
        int amount = request != null ? request.amount() : 10;
        return interactionService.feed(petId, amount);
    }

    @PostMapping("/play")
    public PetResponse play(@PathVariable Long petId,
                            @RequestBody(required = false) InteractionRequest request) {
        int amount = request != null ? request.amount() : 10;
        return interactionService.play(petId, amount);
    }

    @PostMapping("/sleep")
    public PetResponse sleep(@PathVariable Long petId,
                             @RequestBody(required = false) InteractionRequest request) {
        int amount = request != null ? request.amount() : 10;
        return interactionService.sleep(petId, amount);
    }
}
