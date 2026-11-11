package org.example.backend.interaction;

import jakarta.validation.Valid;
import org.example.backend.pet.dto.PetResponse;
import org.example.backend.user.CurrentUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/pets/{petId}")
public class PetInteractionController {

    private static final int DEFAULT_AMOUNT = 10;
    private final PetInteractionService interactions;
    private final CurrentUser currentUser;

    public PetInteractionController(PetInteractionService interactions, CurrentUser currentUser) {
        this.interactions = interactions;
        this.currentUser = currentUser;
    }

    @PostMapping("/feed")
    public PetResponse feed(@PathVariable Long petId, Principal principal,
                            @Valid @RequestBody(required = false) InteractionRequest request) {
        return interactions.feed(petId, currentUser.from(principal), amountOf(request));
    }

    @PostMapping("/play")  /* same shape → interactions.play  */
    @PostMapping("/sleep") /* same shape → interactions.sleep */

    private static int amountOf(InteractionRequest request) {
        return request != null ? request.amount() : DEFAULT_AMOUNT;
    }
}
