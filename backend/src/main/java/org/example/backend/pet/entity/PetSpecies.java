package org.example.backend.pet.entity;

import org.example.backend.personality.pet.PetPersonality;

public enum PetSpecies {
    CAT("cat", "A small, whiskered creature. Looks like the persian cat from Littlest Pet Shop. Big, sparkly eyes. White fur. Well-maintained coat. Very cute in appearance and knows it.", PetPersonality.SPOILED),
    SLIME("slime", "A gooey, slow-moving creature made of ectoplasm. Hard to say which virtual world this one stems from. Could be from anywhere. Doesn't like being touched. Tends to drag itself and leaves a trail of ectoplasm.", PetPersonality.DOCILE),
    TAMAGOTCHI("tamagotchi", "The literal tamagotchi device itself. A small blue digital keychain with a cute cartoon design painted over the plastic shell. Kept in its plastic prison to seal away that which lurks within. Looks innocuous despite what resides inside.", PetPersonality.CONTEMPLATIVE),
    ELECTRIC_RAT("electric_rat", "Bears a lot of resemblance to a certain yellow electric mouse creature from a pocket-monster collecting game. Finds the topic of said resemblance tedious and talks around it at any costs. Small yellow electric creature.", PetPersonality.ENERGETIC);

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

