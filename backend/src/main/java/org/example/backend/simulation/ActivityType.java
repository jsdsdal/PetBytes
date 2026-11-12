package org.example.backend.simulation;

public enum ActivityType {
    FED("You fed the pet.", "The pet ate food from its bowl."),
    NAPPED("You tucked the pet in for a nap.", "The pet took a nap."),
    PLAYED("You played with the pet.", "The pet played on its own."),
    PETTED("You petted the pet.", "The pet was petted."),
    ADOPTED("You adopted the pet!", "The pet was adopted.");

    private final String playerDescription;
    private final String petDescription;

    ActivityType(String playerDescription, String petDescription) {
        this.playerDescription = playerDescription;
        this.petDescription = petDescription;
    }

    // returnerer korrekt string afh. hvem starter interaktionen
    public String getDescription(boolean playerInitiated) {
        return playerInitiated ? playerDescription : petDescription;
    }
}