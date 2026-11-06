package org.example.backend.pet.entity;

import org.example.backend.personality.pet.PetPersonality;

public enum PetSpecies {
    CAT("cat", "A small, whiskered creature.", PetPersonality.SPOILED),
    FOX("fox", "A sharp-eared, bushy-tailed creature.", PetPersonality.ENERGETIC),
    OWL("owl", "A round-eyed, feathered creature.", PetPersonality.CONTEMPLATIVE),
    TURTLE("turtle", "A slow, shelled creature.", PetPersonality.DOCILE);

    private final String designKey;
    private final String description;
    private final PetPersonality homePersonality;

    PetSpecies(String designKey, String description, PetPersonality homePersonality) {
        this.designKey = designKey;
        this.description = description;
        this.homePersonality = homePersonality;
    }

    public String getDesignKey() { return designKey; }
    public String getDescription() { return description; }
    public PetPersonality getPersonality() { return homePersonality; }
}

