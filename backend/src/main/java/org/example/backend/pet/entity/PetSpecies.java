package org.example.backend.pet.entity;

import org.example.backend.personality.pet.PetPersonality;

public enum PetSpecies {
    CAT("cat", "A small, whiskered creature.", PetPersonality.SENSITIVE),
    FOX("fox", "A sharp-eared, bushy-tailed creature.", PetPersonality.CAREUL),
    OWL("owl", "A round-eyed, feathered creature.", PetPersonality.CONTEMPLATIVE),
    TURTLE("turtle", "A slow, shelled creature.", PetPersonality.RAMBUNCTIOUS);

    private final String designKey;
    private final String description;
    private final PetPersonality personality;

    PetSpecies(String designKey, String description, PetPersonality personality) {
        this.designKey = designKey;
        this.description = description;
        this.personality = personality;
    }

    public String getDesignKey() { return designKey; }
    public String getDescription() { return description; }
    public PetPersonality getPersonality() { return personality; }
}

