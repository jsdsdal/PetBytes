package org.example.backend.adoption;

import jakarta.validation.Valid;
import org.example.backend.adoption.dto.AcceptOfferRequest;
import org.example.backend.adoption.dto.AdoptionOfferResponse;
import org.example.backend.adoption.dto.AdoptionOffersResponse;
import org.example.backend.pet.dto.PetResponse;
import org.example.backend.user.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/adoption")
public class AdoptionController {

    private final AdoptionService adoption;
    private final CurrentUser currentUser;

    public AdoptionController(AdoptionService adoption, CurrentUser currentUser) {
        this.adoption = adoption;
        this.currentUser = currentUser;
    }

    @GetMapping("/offers")
    public AdoptionOffersResponse current(Principal principal) {
        return adoption.currentBatch(currentUser.from(principal));
    }

    @PostMapping("/offers/reroll")
    public AdoptionOffersResponse reroll(Principal principal) {
        return adoption.reroll(currentUser.from(principal));
    }

    @PostMapping("/offers/{offerId}/accept")
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse accept(@PathVariable Long offerId,
                              @Valid @RequestBody AcceptOfferRequest request,
                              Principal principal) {
        return adoption.accept(currentUser.from(principal), offerId, request.name());
    }
}