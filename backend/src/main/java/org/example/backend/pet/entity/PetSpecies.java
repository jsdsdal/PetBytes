package org.example.backend.pet.entity;

import org.example.backend.personality.pet.PetPersonality;

public enum PetSpecies {
    CAT("cat", "A small, whiskered creature. Looks like the persian cat from Littlest Pet Shop. Big, sparkly eyes. White fur. Well-maintained coat.", PetPersonality.SPOILED),
    SLIME("slime", "A gooey, slow-moving creature made of ectoplasm. Hard to say which virtual world this one stems from. A bit gross.. and self-aware about that fact.", PetPersonality.DOCILE),
    TAMAGOTCHI("tamagotchi", "The literal tamagotchi device itself. Kept in its plastic prison to seal away that which lurks within. Looks innocuous despite what lurks within.", PetPersonality.CONTEMPLATIVE),
    ELECTRIC_RAT("electric_rat", "Looks like a certain yellow electric mouse creature from a pocket-monster collecting game. Do NOT ever actually refer to said monster. This is definitely not that one. In fact, it's missing the tail. (Pikachu parody - don't ever mention pikachu directly. Make vague references to it without ever giving it away, like you're dodging legal action.)", PetPersonality.ENERGETIC);

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

