package org.example.backend.adoption.dto;

import org.example.backend.adoption.AdoptionOffer;
import org.example.backend.personality.pet.PetPersonality;
import org.example.backend.pet.entity.PetSpecies;

public record AdoptionOfferResponse(
        Long id, PetSpecies species, PetPersonality personality, boolean variant) {

    public static AdoptionOfferResponse from(AdoptionOffer offer) {
        return new AdoptionOfferResponse(offer.getId(), offer.getSpecies(),
                offer.getPersonality(), offer.isVariant());
    }
}
