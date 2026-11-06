package org.example.backend.simulation;

public enum ActivityType {
    FED("The pet ate food from its bowl."),
    NAPPED("The pet took a nap."),
    PLAYED("The pet played on its own."),
    PETTED("The player petted the pet.");

    private final String description;

    ActivityType(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}

