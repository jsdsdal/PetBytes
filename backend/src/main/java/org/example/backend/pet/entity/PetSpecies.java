package org.example.backend.pet.entity;

public enum PetSpecies {
    CAT("cat", "A small, whiskered creature."),
    FOX("fox", "A sharp-eared, bushy-tailed creature."),
    OWL("owl", "A round-eyed, feathered creature."),
    TURTLE("turtle", "A slow, shelled creature.");

    private final String designKey;
    private final String description;

    PetSpecies(String designKey, String description) {
        this.designKey = designKey;
        this.description = description;
    }

    public String getDesignKey() { return designKey; }
    public String getDescription() { return description; }
}

